package tools.jackson.jakarta.rs.json.dw;

import java.io.IOException;
import java.net.*;

import jakarta.servlet.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;

import org.junit.jupiter.api.Test;

import org.eclipse.jetty.server.Server;

import tools.jackson.databind.ObjectWriter;
import tools.jackson.jakarta.rs.cfg.EndpointConfigBase;
import tools.jackson.jakarta.rs.cfg.ObjectWriterInjector;
import tools.jackson.jakarta.rs.cfg.ObjectWriterModifier;

import static org.junit.jupiter.api.Assertions.*;

public abstract class WriteModificationsTestBase extends ResourceTestBase
{
    final static int TEST_PORT = 6021;

    final static int TEST_PORT2 = 6022;
    
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
        @Produces(MediaType.APPLICATION_JSON)
        public Point getPoint() {
            return new Point(1, 2);
        }
    }

    @Path("/point")
    public static class IndentingResource
    {
        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public Point getPoint(@QueryParam("indent") String indent) {
            if ("true".equals(indent)) {
                ObjectWriterInjector.set(new IndentingModifier(true));
            } else {
                ObjectWriterInjector.set(new IndentingModifier(false));
            }
            return new Point(1, 2);
        }
    }
    
    public static class SimpleResourceApp extends JsonApplicationWithJackson {
        public SimpleResourceApp() { super(new SimpleResource()); }
    }

    public static class SimpleIndentingApp extends JsonApplicationWithJackson {
        public SimpleIndentingApp() { super(new IndentingResource()); }
    }
    
    public static class IndentingModifier extends ObjectWriterModifier
    {
        public static boolean doIndent = false;

        public final Boolean _indent;
        
        public IndentingModifier() {
            this(null);
        }

        public IndentingModifier(Boolean indent) {
            _indent = indent;
        }
        
        @Override
        public ObjectWriter modify(EndpointConfigBase<?> endpoint,
                MultivaluedMap<String, Object> httpHeaders,
                Object valueToWrite, ObjectWriter w)
        {
            if (_indent != null) {
                if (_indent.booleanValue()) {
                    w = w.withDefaultPrettyPrinter();
                }
            } else {
                if (doIndent) {
                    w = w.withDefaultPrettyPrinter();
                }
            }
            return w;
        }
    }

    public static class InjectingFilter implements Filter
    {
        @Override
        public void init(FilterConfig filterConfig) throws ServletException { }

        @Override
        public void doFilter(ServletRequest request, ServletResponse response,
                FilterChain chain) throws IOException, ServletException
        {
            ObjectWriterInjector.set(new IndentingModifier());
            chain.doFilter(request, response);
        }

        @Override
        public void destroy() { }
    }
    
    /*
    /**********************************************************************
    /* Test methods, baseline
    /**********************************************************************
     */

    public void testNoIndentWithFilter() throws Exception
    {
        // We need a filter to inject modifier that enables
        Server server = startServer(TEST_PORT, SimpleResourceApp.class,
                InjectingFilter.class);
        final URL url = new URL("http://localhost:"+TEST_PORT+"/point");

        try {
            IndentingModifier.doIndent = false;
            String json = readUTF8(url.openStream());
            assertEquals(a2q("{'x':1,'y':2}"), json);
        } finally {
            server.stop();
        }
    }
    
    public void testNoIndentWithResource() throws Exception
    {
        // We need a filter to inject modifier that enables
        Server server = startServer(TEST_PORT2, SimpleIndentingApp.class);
        final String URL_BASE = "http://localhost:"+TEST_PORT2+"/point";

        try {
            String json = readUTF8(new URL(URL_BASE).openStream());
            assertEquals(a2q("{'x':1,'y':2}"), json);
        } finally {
            server.stop();
        }
    }

    /*
    /**********************************************************************
    /* Test methods, indenting
    /**********************************************************************
     */
    
    /**
     * Test in which writer/generator modification is handled by
     * changing state from Servlet Filter.
     */
    @Test
    public void testIndentationWithFilter() throws Exception
    {
        // We need a filter to inject modifier that enables
        Server server = startServer(TEST_PORT, SimpleResourceApp.class,
                InjectingFilter.class);
        final URL url = new URL("http://localhost:"+TEST_PORT+"/point");

        try {
            IndentingModifier.doIndent = true;
            String json = readUTF8(url.openStream());
            assertEquals(a2q("{\n  'x' : 1,\n  'y' : 2\n}"), json);
        } finally {
            server.stop();
        }
    }

    /**
     * Test in which output writer/generator is modified by assignment from
     * resource method itself.
     */
    @Test
    public void testIndentationWithResource() throws Exception
    {
        // We need a filter to inject modifier that enables
        Server server = startServer(TEST_PORT2, SimpleIndentingApp.class);
        final String URL_BASE = "http://localhost:"+TEST_PORT2+"/point";

        try {
            String json = readUTF8(new URL(URL_BASE + "?indent=true").openStream());
            assertEquals(a2q("{\n  'x' : 1,\n  'y' : 2\n}"), json);
        } finally {
            server.stop();
        }
    }
}
