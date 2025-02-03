package tools.jackson.jakarta.rs.base.cfg;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Comparator;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.annotation.*;
import tools.jackson.databind.annotation.JsonDeserialize;
import tools.jackson.databind.annotation.JsonSerialize;
import tools.jackson.jakarta.rs.base.BaseTestBase;
import tools.jackson.jakarta.rs.cfg.AnnotationBundleKey;

import static org.junit.jupiter.api.Assertions.*;

// for [jaxrs-providers#111]
public class AnnotationBundleKeyTest
    extends BaseTestBase
{
    // let's also test multiple annotation case
    @JsonIgnoreProperties
    @JsonPropertyOrder({ "a", "b" })
    @JsonSerialize
    @JsonDeserialize
    static class Helper {
        @JsonCreator
        public Helper(@JsonProperty("x") int x) { }

        @JsonValue
        @JsonView(Object.class)
        public int getX() { return 3; }

        // A "copy" of `getX`
        @JsonValue
        @JsonView(Object.class)
        public int altX() { return 3; }

        @JsonPropertyOrder
        @JsonView(Object.class)
        public int notX() { return 4; }
        
        public void setX(@JsonProperty("x") int x) { }
    }

    @Test
    public void testWithClassAnnotations() throws Exception
    {
        _checkWith(Helper.class.getAnnotations(), Helper.class.getAnnotations());
    }

    @Test
    public void testWithMethodAnnotationEquals() throws Exception
    {
        // First, same method parameters definitely should match
        _checkWith(Helper.class.getDeclaredMethod("getX").getAnnotations(),
                Helper.class.getDeclaredMethod("getX").getAnnotations());
        // but so should annotations from different method as long as
        // same parameters are in same order
        _checkWith(Helper.class.getDeclaredMethod("getX").getAnnotations(),
                Helper.class.getDeclaredMethod("altX").getAnnotations());
    }

    @Test
    public void testWithMethodAnnotationDifferent() throws Exception
    {
        // However: not so with actually differing annotations
        _checkNotEqual(Helper.class.getDeclaredMethod("getX").getAnnotations(),
                Helper.class.getDeclaredMethod("notX").getAnnotations());
    }

    @Test
    public void testWithMethodParameterAnnotation() throws Exception
    {
        _checkWith(Helper.class.getDeclaredMethod("setX", Integer.TYPE).getParameterAnnotations()[0],
                Helper.class.getDeclaredMethod("setX", Integer.TYPE).getParameterAnnotations()[0]);
    }

    @Test
    public void testWithConstructorAnnotation() throws Exception
    {
        _checkWith(Helper.class.getConstructor(Integer.TYPE).getAnnotations(),
                Helper.class.getConstructor(Integer.TYPE).getAnnotations());
    }
    
    @Test
    public void testWithConstructorParameterAnnotation() throws Exception
    {
        _checkWith(Helper.class.getConstructor(Integer.TYPE).getParameterAnnotations()[0],
                Helper.class.getConstructor(Integer.TYPE).getParameterAnnotations()[0]);
    }

    protected void _checkWith(Annotation[] anns1, Annotation[] anns2) {
        // First, sanity check2 to know we passed non-empty annotations, same by equality
        if (anns1.length == 0) {
            fail("Internal error: empty annotation array");
        }
        Arrays.sort(anns1, Comparator.comparing(Annotation::toString));
        Arrays.sort(anns2, Comparator.comparing(Annotation::toString));
        assertArrayEquals(anns1, anns2, "Internal error: should never differ");

        AnnotationBundleKey b1 = new AnnotationBundleKey(anns1, Object.class);
        AnnotationBundleKey b2 = new AnnotationBundleKey(anns2, Object.class);

        if (!b1.equals(b2) || !b2.equals(b1)) {
            assertEquals(b1, b2,String.format("Implementations over %s backed annotations differ", anns1[0].getClass()));
        }
    }

    protected void _checkNotEqual(Annotation[] anns1, Annotation[] anns2) {
        AnnotationBundleKey b1 = new AnnotationBundleKey(anns1, Object.class);
        AnnotationBundleKey b2 = new AnnotationBundleKey(anns2, Object.class);

        if (b1.equals(b2) || b2.equals(b1)) {
            assertNotEquals(b1, b2,
                    String.format("Implementations over %s backed annotations SHOULD differ but won't", anns1[0].getClass()));
        }
    }
}
