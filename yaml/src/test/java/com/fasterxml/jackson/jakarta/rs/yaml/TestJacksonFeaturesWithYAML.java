package com.fasterxml.jackson.jakarta.rs.yaml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.annotation.*;
import java.lang.reflect.Method;

import jakarta.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializationFeature;

import com.fasterxml.jackson.jakarta.rs.annotation.JacksonFeatures;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for addition of {@link JacksonFeatures}.
 */
public class TestJacksonFeaturesWithYAML extends JakartaRSTestBase
{
    static class Bean {
        public int a = 3;
    }

    @JacksonFeatures(serializationEnable={ SerializationFeature.WRAP_ROOT_VALUE })
    public void writeConfig() { }
        
    @JacksonFeatures(deserializationDisable={ DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES })
    public void readConfig() { }

    // Also, let's check that we can bundle annotations
    @JacksonAnnotationsInside
    @JacksonFeatures(serializationEnable={ SerializationFeature.WRAP_ROOT_VALUE })
    @Target(ElementType.METHOD)
    @Retention(RetentionPolicy.RUNTIME)
    @interface FeatureBundle { }

    @FeatureBundle // should work as if all annotations from FeatureBundle were directly added
    public void writeConfig2() { }
    
    /*
    /**********************************************************
    /* Test methods
    /**********************************************************
     */

    // [Issue-2], serialization
    @Test
    public void testWriteConfigs() throws Exception
    {
        JacksonYAMLProvider prov = new JacksonYAMLProvider();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Bean bean = new Bean();
        Method m = getClass().getDeclaredMethod("writeConfig");
        JacksonFeatures feats = m.getAnnotation(JacksonFeatures.class);
        assertNotNull(feats); // just a sanity check

        try {
            prov.writeTo(bean, bean.getClass(), bean.getClass(), new Annotation[] { feats },
                    MediaType.APPLICATION_JSON_TYPE, null, out);
        } catch (Exception e) {
            throw _unwrap(e);
        }

        assertEquals("---\nBean:\n  a: 3\n", out.toString("UTF-8"));

        out.reset();
        prov.writeTo(bean, bean.getClass(), bean.getClass(), new Annotation[] { },
                MediaType.APPLICATION_JSON_TYPE, null, out);
        assertEquals("---\na: 3\n", out.toString("UTF-8"));
    }
    
    @Test
    public void testWriteConfigsViaBundle() throws Exception
    {
        JacksonYAMLProvider prov = new JacksonYAMLProvider();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Bean bean = new Bean();
        Method m = getClass().getDeclaredMethod("writeConfig2");
        // should still enable root-wrapping
        prov.writeTo(bean, bean.getClass(), bean.getClass(), m.getAnnotations(),
                MediaType.APPLICATION_JSON_TYPE, null, out);

        assertEquals("---\nBean:\n  a: 3\n", out.toString("UTF-8"));
    }
    
    // [Issue-2], deserialization
    @Test
    public void testReadConfigs() throws Exception
    {
        JacksonYAMLProvider prov = new JacksonYAMLProvider();
        Method m = getClass().getDeclaredMethod("readConfig");
        JacksonFeatures feats = m.getAnnotation(JacksonFeatures.class);
        assertNotNull(feats); // just a sanity check

        // ok: here let's verify that we can disable exception throwing unrecognized things
        @SuppressWarnings("unchecked")
        Class<Object> raw = (Class<Object>)(Class<?>)Bean.class;
        Object ob = prov.readFrom(raw, raw,
                new Annotation[] { feats },
                MediaType.APPLICATION_JSON_TYPE, null,
                new ByteArrayInputStream("---\nBean:\n  foobar: 3\n".getBytes("UTF-8")));
        assertNotNull(ob);

        // but without setting, get the exception
        try {
            prov.readFrom(raw, raw,
                    new Annotation[] { },
                    MediaType.APPLICATION_JSON_TYPE, null,
                    new ByteArrayInputStream("---\nBean:\n  foobar: 3\n".getBytes("UTF-8")));
            fail("Should have caught an exception");
        } catch (JsonMappingException e) {
            verifyException(e, "Unrecognized field");
        }
    }

    protected Exception _unwrap(Exception e) {
        while (e.getCause() instanceof Exception) {
            e = (Exception) e.getCause();
        }
        return e;
    }
}
