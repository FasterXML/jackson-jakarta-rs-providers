package com.fasterxml.jackson.jakarta.rs.json;

import java.io.*;
import java.lang.annotation.Annotation;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.StreamingOutput;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestStreamingOutput extends JakartaRSTestBase
{
    static class StreamingSubtype implements StreamingOutput
    {
        // important: this can trick "canSerialize()" to include it:
        public int getFoo() { return 3; }

        @Override
        public void write(OutputStream out) throws IOException {
            out.write("OK".getBytes("UTF-8"));
        }
    }
    
    /*
    /**********************************************************************
    /* Test methods
    /**********************************************************************
     */

    @Test
    public void testSimpleSubtype() throws Exception
    {
        JacksonJsonProvider prov = new JacksonJsonProvider();
        assertFalse(prov.isWriteable(StreamingSubtype.class, StreamingSubtype.class,
                new Annotation[] { }, MediaType.APPLICATION_JSON_TYPE));
    }
}
