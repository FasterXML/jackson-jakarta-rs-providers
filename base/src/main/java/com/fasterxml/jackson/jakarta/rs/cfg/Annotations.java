package com.fasterxml.jackson.jakarta.rs.cfg;

/**
 * Enumeration that defines standard annotation sets available for configuring
 * data binding aspects.
 */
public enum Annotations {
    /**
     * Standard Jackson annotations, defined in Jackson core and databind
     * packages
     */
    JACKSON,

    /**
     * Standard Jakarta XmlBind annotations, used in a way that approximates expected
     * definitions (since XmlBind defines XML aspects, not all features map
     * well to JSON handling)
     */
    JAKARTA_XML_BIND // note: before jakarta known as "JAXB"
    ;
}
