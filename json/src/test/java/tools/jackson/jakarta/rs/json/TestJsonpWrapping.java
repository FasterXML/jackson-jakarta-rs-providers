package tools.jackson.jakarta.rs.json;

import java.io.*;
import java.lang.annotation.Annotation;

import jakarta.ws.rs.core.MediaType;
import tools.jackson.jakarta.rs.json.JacksonJsonProvider;

public class TestJsonpWrapping
    extends JakartaRSTestBase
{
    public void testSimple() throws Exception
    {
        JacksonJsonProvider prov = new JacksonJsonProvider();
        Object bean = new Integer[] { 1, 2, 3 };

        // First: no JSONP wrapping:
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        prov.writeTo(bean, bean.getClass(), bean.getClass(), new Annotation[0], MediaType.APPLICATION_JSON_TYPE, null, out);
        assertEquals("[1,2,3]", out.toString("UTF-8"));
        
        // then with wrapping:
        prov = new JacksonJsonProvider();
        prov.setJSONPFunctionName("addAll");
        out = new ByteArrayOutputStream();
        prov.writeTo(bean, bean.getClass(), bean.getClass(), new Annotation[0], MediaType.APPLICATION_JSON_TYPE, null, out);
        assertEquals("addAll([1,2,3])", out.toString("UTF-8"));
    }
}
