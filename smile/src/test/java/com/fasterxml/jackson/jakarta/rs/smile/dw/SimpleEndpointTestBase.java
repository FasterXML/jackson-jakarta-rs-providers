package com.fasterxml.jackson.jakarta.rs.smile.dw;

import java.io.*;
import java.net.*;

import jakarta.ws.rs.*;

import org.eclipse.jetty.server.Server;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.dataformat.smile.databind.SmileMapper;

import com.fasterxml.jackson.jakarta.rs.smile.SmileMediaTypes;

import static org.junit.jupiter.api.Assertions.*;

public abstract class SimpleEndpointTestBase extends ResourceTestBase
{
    final static int TEST_PORT = 6011;
    
    static class Point {
        public int x, y;

        protected Point() { }
        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    @Path("/point")
    public static class SimpleResource
    {
        @GET
        @Produces({ SmileMediaTypes.APPLICATION_JACKSON_SMILE })
        public Point getPoint() {
            return new Point(1, 2);
        }

        @Path("/custom")
        @GET
        @Produces({ "application/vnd.com.example.v1+smile" })
        public Point getPointCustom() {
            return new Point(1, 2);
        }
    }

    public static class SimpleResourceApp extends SmileApplicationWithJackson {
        public SimpleResourceApp() { super(new SimpleResource()); }
    }

    final static byte[] UNTOUCHABLE_RESPONSE = new byte[] { 1, 2, 3, 4 };

    @Path("/raw")
    public static class RawResource
    {
        @GET
        @Path("bytes")
        @Produces({ SmileMediaTypes.APPLICATION_JACKSON_SMILE })
        public byte[] getBytes() throws IOException {
            return UNTOUCHABLE_RESPONSE;
        }
    }

    public static class SimpleRawApp extends SmileApplicationWithJackson {
        public SimpleRawApp() { super(new RawResource()); }
    }

    /*
    /**********************************************************************
    /* Test methods
    /**********************************************************************
     */

    @Test
    public void testStandardSmile() throws Exception
    {
        final ObjectMapper mapper = new SmileMapper();
        Server server = startServer(TEST_PORT, SimpleResourceApp.class);
        Point p;

        try {
            InputStream in = new URL("http://localhost:"+TEST_PORT+"/point").openStream();
            p = mapper.readValue(in, Point.class);
            in.close();
        } finally {
            server.stop();
        }
        // ensure we got a valid Point
        assertNotNull(p);
        assertEquals(1, p.x);
        assertEquals(2, p.y);
    }

    @Test
    public void testCustomMediaTypeWithSmileExtension() throws Exception
    {
        final ObjectMapper mapper = new SmileMapper();
        Server server = startServer(TEST_PORT, SimpleResourceApp.class);
        Point p;

        try {
            final URL url = new URL("http://localhost:" + TEST_PORT + "/point/custom");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "application/vnd.com.example.v1+smile");
            InputStream in = conn.getInputStream();
            p = mapper.readValue(in, Point.class);
            in.close();
        } finally {
            server.stop();
        }
        // ensure we got a valid Point
        assertNotNull(p);
        assertEquals(1, p.x);
        assertEquals(2, p.y);
    }

    // [Issue#34] Verify that Untouchables act the way as they should
    @SuppressWarnings("resource")
    @Test
    public void testUntouchables() throws Exception
    {
        Server server = startServer(TEST_PORT, SimpleRawApp.class);
        try {
            InputStream in = new URL("http://localhost:"+TEST_PORT+"/raw/bytes").openStream();
            assertArrayEquals(UNTOUCHABLE_RESPONSE, readAll(in));
        } finally {
            server.stop();
        }
    }
}
