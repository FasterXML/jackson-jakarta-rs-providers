module tools.jackson.jakarta.rs.cbor {
    exports tools.jackson.jakarta.rs.cbor;
    // CXF, RESTEasy, OpenAPI require reflective access
    opens tools.jackson.jakarta.rs.cbor;

    requires tools.jackson.core;
    requires tools.jackson.databind;
    requires tools.jackson.dataformat.cbor;
    requires tools.jackson.module.jakarta.xmlbind;

    requires tools.jackson.jakarta.rs.base;

    provides jakarta.ws.rs.ext.MessageBodyReader with
        tools.jackson.jakarta.rs.cbor.JacksonCBORProvider;
    provides jakarta.ws.rs.ext.MessageBodyWriter with
        tools.jackson.jakarta.rs.cbor.JacksonCBORProvider;
}
