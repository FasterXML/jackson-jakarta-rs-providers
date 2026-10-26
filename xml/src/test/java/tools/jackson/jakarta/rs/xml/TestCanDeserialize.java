package tools.jackson.jakarta.rs.xml;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import jakarta.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.MappingIterator;
import tools.jackson.databind.exc.MismatchedInputException;
import tools.jackson.jakarta.rs.cfg.JakartaRSFeature;

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
    @Test
    public void testCanDeserializeMappingIterator() throws IOException
    {
        MappingIterator<Bean> it = _readIterator(new JacksonXMLProvider(),
                "<List><item><x>1</x></item><item><x>2</x></item></List>");
        assertTrue(it.hasNextValue());
        assertEquals(1, it.nextValue().x);
        assertTrue(it.hasNextValue());
        assertEquals(2, it.nextValue().x);
        assertFalse(it.hasNextValue());
    }

    // [jakarta-rs-providers#69]: but this means that a single (non-list) root element
    // is also unwrapped, and fails to bind (same as with `XmlMapper.readValues()`)
    @Test
    public void testMappingIteratorSingleRootFails() throws IOException
    {
        MappingIterator<Bean> it = _readIterator(new JacksonXMLProvider(),
                "<Bean><x>3</x></Bean>");
        assertThrows(MismatchedInputException.class, () -> it.readAll());
    }

    // [jakarta-rs-providers#69]: ... unless unwrapping is disabled
    @Test
    public void testMappingIteratorSingleRootNoUnwrap() throws IOException
    {
        JacksonXMLProvider prov = new JacksonXMLProvider()
                .disable(JakartaRSFeature.UNWRAP_ROOT_ARRAY_FOR_ITERATOR);
        MappingIterator<Bean> it = _readIterator(prov, "<Bean><x>3</x></Bean>");
        assertTrue(it.hasNextValue());
        assertEquals(3, it.nextValue().x);
        assertFalse(it.hasNextValue());
    }

    @SuppressWarnings("unchecked")
    private MappingIterator<Bean> _readIterator(JacksonXMLProvider prov, String xml)
        throws IOException
    {
        Type type = new TypeReference<MappingIterator<Bean>>() { }.getType();
        return (MappingIterator<Bean>) prov.readFrom(
                (Class<Object>) (Class<?>) MappingIterator.class, type, new Annotation[0],
                MediaType.APPLICATION_XML_TYPE, null, new ByteArrayInputStream(xml.getBytes()));
    }
}
