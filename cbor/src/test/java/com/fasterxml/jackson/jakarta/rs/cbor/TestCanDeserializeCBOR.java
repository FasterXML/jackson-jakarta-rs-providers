package com.fasterxml.jackson.jakarta.rs.cbor;

import java.io.*;
import java.lang.annotation.Annotation;

import jakarta.ws.rs.core.MediaType;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test to check [JACKSON-540]
 */
public class TestCanDeserializeCBOR extends JakartaRSTestBase
{
    static class Bean {
        public int x;
    }

    // [Issue#1]: exception for no content
    @Test
    public void testCanSerializeEmpty() throws IOException
    {
        JacksonCBORProvider prov = new JacksonCBORProvider();
        Bean b = (Bean) prov.readFrom(Object.class, Bean.class, new Annotation[0],
                MediaType.APPLICATION_XML_TYPE, null, new ByteArrayInputStream(new byte[0]));
        assertNull(b);
    }
}
