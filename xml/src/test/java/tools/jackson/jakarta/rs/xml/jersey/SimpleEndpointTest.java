package tools.jackson.jakarta.rs.xml.jersey;

import jakarta.servlet.Servlet;
import tools.jackson.jakarta.rs.xml.dw.SimpleEndpointTestBase;

import org.glassfish.jersey.servlet.ServletContainer;

public class SimpleEndpointTest extends SimpleEndpointTestBase {
    @Override
    protected Class<? extends Servlet> servletContainerClass() { return ServletContainer.class; }
}
