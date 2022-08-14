package tools.jackson.jakarta.rs.json.jersey;

import jakarta.servlet.Servlet;
import tools.jackson.jakarta.rs.json.dw.SimpleEndpointTestBase;

import org.glassfish.jersey.servlet.ServletContainer;

public class SimpleJerseyEndpointTest extends SimpleEndpointTestBase {
    @Override
    protected Class<? extends Servlet> servletContainerClass() { return ServletContainer.class; }
}
