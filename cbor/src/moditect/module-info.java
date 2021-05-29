// Originally generated using Moditect maven plugin, last mod 14-Oct-2020
module com.fasterxml.jackson.jaxrs.cbor {
    exports com.fasterxml.jackson.jaxrs.cbor;
    // [jaxrs-providers#119]: CXF, RESTEasy, OpenAPI require reflective access
    opens com.fasterxml.jackson.jakarta.rs.cbor;

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.cbor;
    requires com.fasterxml.jackson.module.jakarta.xmlbind;

    requires com.fasterxml.jackson.jax.rs.base;

    requires static jakarta.ws.rs;
    requires static jakarta.ws.rs.api;

    provides jakarta.ws.rs.ext.MessageBodyReader with
        com.fasterxml.jackson.jakarta.rs.cbor.JacksonCBORProvider;
    provides jakarta.ws.rs.ext.MessageBodyWriter with
        com.fasterxml.jackson.jakarta.rs.cbor.JacksonCBORProvider;
}
