package tools.jackson.jakarta.rs.json.jersey;

import jakarta.servlet.Servlet;
import tools.jackson.jakarta.rs.json.dw.AnnotationTestBase;

import org.glassfish.jersey.servlet.ServletContainer;

public class AnnotationTest extends AnnotationTestBase {
    @Override
    protected Class<? extends Servlet> servletContainerClass() { return ServletContainer.class; }
}
