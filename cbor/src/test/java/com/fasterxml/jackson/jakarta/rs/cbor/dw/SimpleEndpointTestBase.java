package com.fasterxml.jackson.jakarta.rs.cbor.dw;

import java.io.*;
import java.net.*;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

import org.eclipse.jetty.server.Server;
import org.junit.Assert;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.dataformat.cbor.databind.CBORMapper;

import com.fasterxml.jackson.jakarta.rs.cbor.CBORMediaTypes;

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
        @Produces({ CBORMediaTypes.APPLICATION_JACKSON_CBOR })
        public Point getPoint() {
            return new Point(1, 2);
        }

        @GET
        @Path("/custom")
        @Produces({ "application/vnd.com.example.v1+cbor" })
        public Point getPointCustom() {
            return new Point(1, 2);
        }
    }

    public static class SimpleResourceApp extends CBORApplicationWithJackson {
        public SimpleResourceApp() { super(new SimpleResource()); }
    }

    final static byte[] UNTOUCHABLE_RESPONSE = new byte[] { 1, 2, 3, 4 };

    @Path("/raw")
    public static class RawResource
    {
        @GET
        @Path("bytes")
        @Produces({ CBORMediaTypes.APPLICATION_JACKSON_CBOR })
        public byte[] getBytes() throws IOException {
            return UNTOUCHABLE_RESPONSE;
        }
    }

    public static class SimpleRawApp extends CBORApplicationWithJackson {
        public SimpleRawApp() { super(new RawResource()); }
    }

    /*
    /**********************************************************************
    /* Test methods
    /**********************************************************************
     */

    public void testSimpleObject() throws Exception
    {
        final ObjectMapper mapper = new CBORMapper();
        Server server = startServer(TEST_PORT, SimpleResourceApp.class);
        Point p;
        final URL url = new URL("http://localhost:"+TEST_PORT+"/point");

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            _verifyResponse(conn);
            try (InputStream in = conn.getInputStream()) {
                p = mapper.readValue(in, Point.class);
            }
        } catch (Exception e) {
            p = null;
        } finally {
            server.stop();
        }
        // ensure we got a valid Point
        assertNotNull(p);
        assertEquals(1, p.x);
        assertEquals(2, p.y);
    }

    public void testCustomMediaTypeWithCborExtension() throws Exception
    {
        final ObjectMapper mapper = new CBORMapper();
        Server server = startServer(TEST_PORT, SimpleResourceApp.class);
        Point p;
        final URL url = new URL("http://localhost:" + TEST_PORT + "/point/custom");

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "application/vnd.com.example.v1+cbor");
            _verifyResponse(conn);
            try (InputStream in = conn.getInputStream()) {
                p = mapper.readValue(in, Point.class);
            }
        } finally {
            server.stop();
        }
        // ensure we got a valid Point
        assertNotNull(p);
        assertEquals(1, p.x);
        assertEquals(2, p.y);
    }

    // [Issue#34] Verify that Untouchables act the way as they should
    public void testUntouchables() throws Exception
    {
        Server server = startServer(TEST_PORT, SimpleRawApp.class);
        final URL url = new URL("http://localhost:"+TEST_PORT+"/raw/bytes");
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            _verifyResponse(conn);
            try (InputStream in = url.openStream()) {
                Assert.assertArrayEquals(UNTOUCHABLE_RESPONSE, readAll(in));
            }
        } finally {
            server.stop();
        }
    }

    protected void _verifyResponse(HttpURLConnection conn) throws IOException {
        int status = conn.getResponseCode();
        if (status >= 200 && status <= 299) {
            return;
        }
        // But otherwise, try to get fail...
        try {
            byte[] failBytes = readAll(conn.getErrorStream());
            fail("Call failed with status code "+status+", problem: ("+failBytes.length+" bytes) "
                    +asUTF8(failBytes, 2000));
        } catch (Exception e) {
            throw new RuntimeException("Failed to read error content for status code "
                    +status+": "+e, e);
        }
    }
}
