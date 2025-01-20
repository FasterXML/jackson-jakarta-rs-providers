// Jakarta-RS JSON module-info for Main artifact
module tools.jackson.jakarta.rs.json
{
    requires tools.jackson.core;
    requires tools.jackson.databind;
    requires tools.jackson.module.jakarta.xmlbind;
    requires tools.jackson.jakarta.rs.base;

    exports tools.jackson.jakarta.rs.json;
    // CXF, RESTEasy, OpenAPI require reflective access
    opens tools.jackson.jakarta.rs.json;

    provides jakarta.ws.rs.ext.MessageBodyReader with
        tools.jackson.jakarta.rs.json.JacksonJsonProvider;
    provides jakarta.ws.rs.ext.MessageBodyWriter with
        tools.jackson.jakarta.rs.json.JacksonJsonProvider;
}
