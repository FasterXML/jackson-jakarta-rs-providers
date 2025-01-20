// Jakarta-RS Smile module-info for Main artifact
module tools.jackson.jakarta.rs.smile
{
    requires tools.jackson.core;
    requires tools.jackson.databind;
    requires tools.jackson.dataformat.smile;
    requires tools.jackson.module.jakarta.xmlbind;
    requires tools.jackson.jakarta.rs.base;

    exports tools.jackson.jakarta.rs.smile;
    // CXF, RESTEasy, OpenAPI require reflective access
    opens tools.jackson.jakarta.rs.smile;

    provides jakarta.ws.rs.ext.MessageBodyReader with
        tools.jackson.jakarta.rs.smile.JacksonSmileProvider;
    provides jakarta.ws.rs.ext.MessageBodyWriter with
        tools.jackson.jakarta.rs.smile.JacksonSmileProvider;
}
