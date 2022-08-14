package tools.jackson.jakarta.rs.yaml.jersey;

import org.glassfish.jersey.servlet.ServletContainer;

import jakarta.servlet.Servlet;
import tools.jackson.jakarta.rs.yaml.dw.SimpleEndpointTestBase;

public class SimpleEndpointTest extends SimpleEndpointTestBase {
    @Override
    protected Class<? extends Servlet> servletContainerClass() { return ServletContainer.class; }
}
