package tools.jackson.jakarta.rs.xml;

import java.io.*;
import java.lang.annotation.Annotation;

import jakarta.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestCanSerialize extends JakartaRSTestBase
{
    static class Bean {
        public int x;
    }
    
    @Test
    public void testCanDeserialize() throws IOException
    {
        JacksonXMLProvider prov = new JacksonXMLProvider();
        String XML = "<Bean><x>3</x></Bean>";
        InputStream stream = new ByteArrayInputStream(XML.getBytes());
        Bean b = (Bean) prov.readFrom(Object.class, Bean.class, new Annotation[0],
                MediaType.APPLICATION_XML_TYPE, null, stream);
        assertNotNull(b);
        assertEquals(3, b.x);
    }

    @Test
    public void testCanDeserializeEmpty() throws IOException
    {
        JacksonXMLProvider prov = new JacksonXMLProvider();
        Bean b = (Bean) prov.readFrom(Object.class, Bean.class, new Annotation[0],
                MediaType.APPLICATION_XML_TYPE, null, new ByteArrayInputStream(new byte[0]));
        assertNull(b);
    }
}
