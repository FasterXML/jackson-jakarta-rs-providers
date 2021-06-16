// Originally generated using Moditect maven plugin, last mod 14-Oct-2020
module com.fasterxml.jackson.jakarta.rs.cbor {
    exports com.fasterxml.jackson.jakarta.rs.cbor;
    // CXF, RESTEasy, OpenAPI require reflective access
    opens com.fasterxml.jackson.jakarta.rs.cbor;

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.cbor;
    requires com.fasterxml.jackson.module.jakarta.xmlbind;

    requires com.fasterxml.jackson.jaxrs.base;

    requires static jakarta.ws.rs;

    provides jakarta.ws.rs.ext.MessageBodyReader with
        com.fasterxml.jackson.jakarta.rs.cbor.JacksonCBORProvider;
    provides jakarta.ws.rs.ext.MessageBodyWriter with
        com.fasterxml.jackson.jakarta.rs.cbor.JacksonCBORProvider;
}
