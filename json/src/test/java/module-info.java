// Jakarta-RS JSON module-info for (unit) Tests
module tools.jackson.jakarta.rs.json
{
    // Since we are not split from Main artifact, will not
    // need to depend on Main artifact -- but need its dependencies

    requires tools.jackson.core;
    requires tools.jackson.databind;
    requires tools.jackson.module.jakarta.xmlbind;

    requires tools.jackson.jakarta.rs.base;

    // Additional test lib/framework dependencies
    requires junit; // JUnit 4

    // Other test deps
    requires jetty.servlet.api;
    requires org.eclipse.jetty.server;
    requires org.eclipse.jetty.servlet;
    // Shouldn't we have "requires" on resteasy too?
    
    // Further, need to open up test packages for JUnit et al
    opens tools.jackson.jakarta.rs.json;
    opens tools.jackson.jakarta.rs.json.dw;
    opens tools.jackson.jakarta.rs.json.jersey;
    opens tools.jackson.jakarta.rs.json.resteasy;
    opens tools.jackson.jakarta.rs.json.testutil;
    opens tools.jackson.jakarta.rs.json.util;
}
