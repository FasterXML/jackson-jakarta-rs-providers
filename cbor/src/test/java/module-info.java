// Jakarta-RS CBOR module-info for (unit) Tests
module tools.jackson.jakarta.rs.cbor
{
    // Since we are not split from Main artifact, will not
    // need to depend on Main artifact -- but need its dependencies

    requires tools.jackson.core;
    requires tools.jackson.databind;
    requires tools.jackson.dataformat.cbor;
    requires tools.jackson.module.jakarta.xmlbind;

    requires tools.jackson.jakarta.rs.base;

    // Additional test lib/framework dependencies
    requires org.junit.jupiter.api;

    // Other test deps
    requires jersey.container.servlet.core;
    requires jetty.servlet.api;
    requires org.eclipse.jetty.server;
    requires org.eclipse.jetty.servlet;
    
    // Further, need to open up test packages for JUnit et al
    opens tools.jackson.jakarta.rs.cbor;
    opens tools.jackson.jakarta.rs.cbor.dw;
    opens tools.jackson.jakarta.rs.cbor.jersey;
}
