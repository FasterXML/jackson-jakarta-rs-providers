package com.fasterxml.jackson.jakarta.rs.yaml.dw;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.eclipse.jetty.server.Server;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.dataformat.smile.databind.SmileMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;

import com.fasterxml.jackson.jakarta.rs.yaml.JacksonYAMLProvider;
import com.fasterxml.jackson.jakarta.rs.yaml.YAMLMediaTypes;
import com.fasterxml.jackson.jakarta.rs.json.JacksonJsonProvider;
import com.fasterxml.jackson.jakarta.rs.smile.JacksonSmileProvider;
import com.fasterxml.jackson.jakarta.rs.smile.SmileMediaTypes;

import static org.junit.jupiter.api.Assertions.*;

public abstract class SimpleEndpointTestBase extends ResourceTestBase
{
    final private static int TEST_PORT = 6011;

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
        @Produces(YAMLMediaTypes.APPLICATION_JACKSON_YAML)
        public Point getPoint() {
            return new Point(1, 2);
        }

        @GET
        @Path("/custom")
        @Produces({ "application/vnd.com.example.v1+yaml" })
        public Point getPointCustom() {
            return new Point(1, 2);
        }
    }

    @Path("/point")
    public static class MultiMediaTypeResource
    {
        @GET
        @Produces({
                YAMLMediaTypes.APPLICATION_JACKSON_YAML,
                "application/vnd.com.example.v1+yaml",
                MediaType.APPLICATION_JSON,
                "application/vnd.com.example.v1+json",
                SmileMediaTypes.APPLICATION_JACKSON_SMILE,
                "application/vnd.com.example.v1+smile"
        })
        public Point getPoint() {
            return new Point(1, 2);
        }
    }

    public static class SimpleResourceApp extends YAMLApplicationWithJackson {
        public SimpleResourceApp() { super(new SimpleResource()); }
    }

    public static class MultiMediaTypeResourceApp extends YAMLApplication {
        public MultiMediaTypeResourceApp() {
            super(new MultiMediaTypeResource(), new JacksonYAMLProvider(), new JacksonJsonProvider(), new JacksonSmileProvider());
        }
    }

    final static byte[] UNTOUCHABLE_RESPONSE = new byte[] { 1, 2, 3, 4 };

    @Path("/raw")
    public static class RawResource
    {
        @GET
        @Path("bytes")
        @Produces({ YAMLMediaTypes.APPLICATION_JACKSON_YAML })
        public byte[] getBytes() throws IOException {
            return UNTOUCHABLE_RESPONSE;
        }
    }

    public static class SimpleRawApp extends YAMLApplicationWithJackson {
        public SimpleRawApp() { super(new RawResource()); }
    }

    /*
    /**********************************************************************
    /* Test methods
    /**********************************************************************
     */

    @Test
    public void testSimplePoint() throws Exception
    {
        final ObjectMapper mapper = new YAMLMapper();
        Server server = startServer(TEST_PORT, SimpleResourceApp.class);
        Point p;
        String yaml = null;
        final URL url = new URL("http://localhost:" + TEST_PORT + "/point");

        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            yaml = _verifyAndReadResponse(conn);
            p = mapper.readValue(yaml, Point.class);
        } finally {
            server.stop();
        }
        // ensure we got a valid Point
        assertNotNull(p);
        assertEquals(1, p.x);
        assertEquals(2, p.y);

        if (!yaml.contains("x: 1")
                || (!yaml.contains("y: 2") && !yaml.contains("\"y\": 2"))
        ) {
            fail("Expected Point to be serialized as YAML, instead got: "+yaml);
        }
    }

    @Test
    public void testCustomMediaTypeWithYamlExtension() throws Exception
    {
        final ObjectMapper mapper = new YAMLMapper();
        Server server = startServer(TEST_PORT, SimpleResourceApp.class);
        Point p;

        try {
            final URL url = new URL("http://localhost:" + TEST_PORT + "/point/custom");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "application/vnd.com.example.v1+yaml");
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

    // Tests that if multiple providers are registered, content negotiation works properly across regular and irregular
    // mime types
    @Test
    public void testMultipleMediaTypes() throws Exception
    {
        Server server = startServer(TEST_PORT, MultiMediaTypeResourceApp.class);
        final URL url = new URL("http://localhost:" + TEST_PORT + "/point");
        Point p;

        final ObjectMapper yamlMapper = new YAMLMapper();
        final ObjectMapper jsonMapper = new ObjectMapper();
        final ObjectMapper smileMapper = new SmileMapper();

        try {
            // Standard YAML
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", YAMLMediaTypes.APPLICATION_JACKSON_YAML);
            InputStream in = conn.getInputStream();
            assertEquals(YAMLMediaTypes.APPLICATION_JACKSON_YAML, conn.getHeaderField("Content-Type"));
            p = yamlMapper.readValue(in, Point.class);
            in.close();
            assertNotNull(p);
            assertEquals(1, p.x);
            assertEquals(2, p.y);

            // Custom media type YAML
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "application/vnd.com.example.v1+yaml");
            in = conn.getInputStream();
            assertEquals("application/vnd.com.example.v1+yaml", conn.getHeaderField("Content-Type"));
            p = yamlMapper.readValue(in, Point.class);
            in.close();
            assertNotNull(p);
            assertEquals(1, p.x);
            assertEquals(2, p.y);


            // Standard JSON
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", MediaType.APPLICATION_JSON);
            in = conn.getInputStream();
            assertEquals(MediaType.APPLICATION_JSON, conn.getHeaderField("Content-Type"));
            p = jsonMapper.readValue(in, Point.class);
            in.close();
            assertNotNull(p);
            assertEquals(1, p.x);
            assertEquals(2, p.y);

            // Custom media type JSON
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "application/vnd.com.example.v1+json");
            in = conn.getInputStream();
            assertEquals("application/vnd.com.example.v1+json", conn.getHeaderField("Content-Type"));
            p = jsonMapper.readValue(in, Point.class);
            in.close();
            assertNotNull(p);
            assertEquals(1, p.x);
            assertEquals(2, p.y);

            // Standard Smile
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", SmileMediaTypes.APPLICATION_JACKSON_SMILE);
            in = conn.getInputStream();
            assertEquals(SmileMediaTypes.APPLICATION_JACKSON_SMILE, conn.getHeaderField("Content-Type"));
            p = smileMapper.readValue(in, Point.class);
            in.close();
            assertNotNull(p);
            assertEquals(1, p.x);
            assertEquals(2, p.y);

            // Custom media type Smile
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "application/vnd.com.example.v1+smile");
            in = conn.getInputStream();
            assertEquals("application/vnd.com.example.v1+smile", conn.getHeaderField("Content-Type"));
            p = smileMapper.readValue(in, Point.class);
            in.close();
            assertNotNull(p);
            assertEquals(1, p.x);
            assertEquals(2, p.y);

            // If everything is acceptable, YAML should be used because it occurs first in the @Produces annotation
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Accept", "*/*");
            in = conn.getInputStream();
            assertEquals(YAMLMediaTypes.APPLICATION_JACKSON_YAML, conn.getHeaderField("Content-Type"));
            p = yamlMapper.readValue(in, Point.class);
            in.close();
            assertNotNull(p);
            assertEquals(1, p.x);
            assertEquals(2, p.y);
        } finally {
            server.stop();
        }
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

    protected String _verifyAndReadResponse(HttpURLConnection conn) throws IOException {
        int status = conn.getResponseCode();
        if (status >= 200 && status <= 299) {
            return readUTF8(conn.getInputStream());
        }
        // But otherwise, try to get fail...
        try {
            byte[] failBytes = readAll(conn.getErrorStream());
            fail("Call failed with status code "+status+", problem: ("+failBytes.length+" bytes) "
                    +asUTF8(failBytes, 2000));
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to read error content for status code "
                    +status+": "+e, e);
        }
    }

}
