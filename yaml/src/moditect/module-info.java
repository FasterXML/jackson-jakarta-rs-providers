// Originally generated using Moditect maven plugin, last mod 14-Oct-2020
module com.fasterxml.jackson.jakarta.rs.yaml {
    exports com.fasterxml.jackson.jakarta.rs.yaml;
    // CXF, RESTEasy, OpenAPI require reflective access
    opens com.fasterxml.jackson.jakarta.rs.yaml;

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.yaml;
    requires com.fasterxml.jackson.module.jakarta.xmlbind;

    requires com.fasterxml.jackson.jaxrs.base;

    requires static jakarta.ws.rs;
    requires static jakarta.ws.rs.api;

    provides jakarta.ws.rs.ext.MessageBodyReader with
        com.fasterxml.jackson.jakarta.rs.yaml.JacksonYAMLProvider;
    provides jakarta.ws.rs.ext.MessageBodyWriter with
        com.fasterxml.jackson.jakarta.rs.yaml.JacksonYAMLProvider;
}
