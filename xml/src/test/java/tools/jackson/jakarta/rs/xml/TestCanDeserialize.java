package tools.jackson.jakarta.rs.xml;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import jakarta.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.MappingIterator;

import static org.junit.jupiter.api.Assertions.*;

public class TestCanDeserialize extends JakartaRSTestBase
{
    static class Bean {
        public int x;
    }

    @Test
    public void testCanSerialize() throws IOException
    {
        JacksonXMLProvider prov = new JacksonXMLProvider();
        String XML = "<Bean><x>3</x></Bean>";
        InputStream stream = new ByteArrayInputStream(XML.getBytes());
        Bean b = (Bean) prov.readFrom(Object.class, Bean.class, new Annotation[0],
                MediaType.APPLICATION_XML_TYPE, null, stream);
        assertNotNull(b);
        assertEquals(3, b.x);
    }

    // [Issue#4]: exception for no content
    @Test
    public void testCanSerializeEmpty() throws IOException
    {
        JacksonXMLProvider prov = new JacksonXMLProvider();
        Bean b = (Bean) prov.readFrom(Object.class, Bean.class, new Annotation[0],
                MediaType.APPLICATION_XML_TYPE, null, new ByteArrayInputStream(new byte[0]));
        assertNull(b);
    }

    // [jakarta-rs-providers#69]: root-level list to be unwrapped for `MappingIterator`
    @SuppressWarnings("unchecked")
    @Test
    public void testCanDeserializeMappingIterator() throws IOException
    {
        JacksonXMLProvider prov = new JacksonXMLProvider();
        String XML = "<List><item><x>1</x></item><item><x>2</x></item></List>";
        Type type = new TypeReference<MappingIterator<Bean>>() { }.getType();
        MappingIterator<Bean> it = (MappingIterator<Bean>) prov.readFrom(
                (Class<Object>) (Class<?>) MappingIterator.class, type, new Annotation[0],
                MediaType.APPLICATION_XML_TYPE, null, new ByteArrayInputStream(XML.getBytes()));
        assertTrue(it.hasNextValue());
        assertEquals(1, it.nextValue().x);
        assertTrue(it.hasNextValue());
        assertEquals(2, it.nextValue().x);
        assertFalse(it.hasNextValue());
    }
}
