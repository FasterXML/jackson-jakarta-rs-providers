// Originally generated using Moditect maven plugin, last mod 14-Oct-2020
module com.fasterxml.jackson.jakarta.rs.xml {
    exports com.fasterxml.jackson.jakarta.rs.xml;
    // CXF, RESTEasy, OpenAPI require reflective access
    opens com.fasterxml.jackson.jakarta.rs.xml;

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.xml;
    requires com.fasterxml.jackson.module.jakarta.xmlbind;

    requires com.fasterxml.jackson.jakarta.rs.base;

    provides jakarta.ws.rs.ext.MessageBodyReader with
        com.fasterxml.jackson.jakarta.rs.xml.JacksonXMLProvider;
    provides jakarta.ws.rs.ext.MessageBodyWriter with
        com.fasterxml.jackson.jakarta.rs.xml.JacksonXMLProvider;
}
