package com.fasterxml.jackson.jakarta.rs.json;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.LinkedHashMap;
import java.util.Map;

import jakarta.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.jakarta.rs.cfg.JakartaRSFeature;

import static org.junit.jupiter.api.Assertions.*;

public class TestCanDeserialize extends JakartaRSTestBase
{
	@SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
	public void testCanDeserialize() throws IOException {
		Map<String, Object> object = new LinkedHashMap<String, Object>();
		JacksonJsonProvider prov = new JacksonJsonProvider();

		String json = "{\"foo\":\"bar\"}";
		InputStream stream = new ByteArrayInputStream(json.getBytes());

		object = (Map) prov.readFrom(Object.class, object.getClass(), new Annotation[0],
				MediaType.APPLICATION_JSON_TYPE, null, stream);

		assertEquals("bar", object.get("foo"));
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
	public void testCanDeserializeEmpty() throws IOException {
		JacksonJsonProvider prov = new JacksonJsonProvider();

		InputStream stream = new ByteArrayInputStream(new byte[0]);
		Class<Object> type = _type(Map.class);

          Map<String, Object> result = (Map) prov.readFrom(type, type, new Annotation[0],
				MediaType.APPLICATION_JSON_TYPE, null, stream);
		
		assertNull(result);
	}

	/**
	 * Unit test for verifying functioning of {@link JakartaRSFeature#ALLOW_EMPTY_INPUT}.
	 */
     @Test
    public void testFailingDeserializeEmpty() throws IOException {
         JacksonJsonProvider prov = new JacksonJsonProvider();
         prov.disable(JakartaRSFeature.ALLOW_EMPTY_INPUT);

         InputStream stream = new ByteArrayInputStream(new byte[0]);
         Class<Object> type = _type(Map.class);
         try {
             prov.readFrom(type, type, new Annotation[0],
                   MediaType.APPLICATION_JSON_TYPE, null, stream);
             fail("Should not succeed with passing of empty input");
         } catch (IOException e) {
             verifyException(e, "no content");
             
             final String clsName = e.getClass().getName();
             if ("jakarta.ws.rs.core.NoContentException".equals(clsName)) {
                 // Ideally, we'd get this
             } else {
                 fail("Unexpected exception type: "+clsName);
             }
         }
    }

    @SuppressWarnings("unchecked")
    private Class<Object> _type(Class<?> cls) {
        return (Class<Object>) cls;
    }
}
