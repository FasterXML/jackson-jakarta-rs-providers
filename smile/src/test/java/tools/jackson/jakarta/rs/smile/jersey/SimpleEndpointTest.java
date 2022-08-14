package tools.jackson.jakarta.rs.smile.jersey;

import jakarta.servlet.Servlet;
import tools.jackson.jakarta.rs.smile.dw.SimpleEndpointTestBase;

import org.glassfish.jersey.servlet.ServletContainer;

public class SimpleEndpointTest extends SimpleEndpointTestBase {
    @Override
    protected Class<? extends Servlet> servletContainerClass() { return ServletContainer.class; }
}
