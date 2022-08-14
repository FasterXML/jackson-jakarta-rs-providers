package tools.jackson.jakarta.rs.json.jersey;

import jakarta.servlet.Servlet;
import tools.jackson.jakarta.rs.json.dw.WriteModificationsTestBase;

import org.glassfish.jersey.servlet.ServletContainer;

public class WriteModificationsTest extends WriteModificationsTestBase {
    @Override
    protected Class<? extends Servlet> servletContainerClass() { return ServletContainer.class; }
}
