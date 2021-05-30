package com.fasterxml.jackson.jakarta.rs.smile;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.jakarta.rs.cfg.Annotations;

/**
 * JSON content type provider automatically configured to use both Jackson
 * and Jakarta XmlBind annotations (in that order of priority).
 * Otherwise functionally same as {@link JacksonSmileProvider}.
 *<p>
 * Typical usage pattern is to just instantiate instance of this
 * provider for Jakarta-RS and use as is: this will use both Jackson and
 * Jakarta XmlBind annotations (with Jackson annotations having priority).
 *<p>
 * Note: class annotations are duplicated from super class, since it
 * is not clear whether Jakarta-RS implementations are required to
 * check settings of super-classes. It is important to keep annotations
 * in sync if changed.
 */
@Provider
@Consumes(MediaType.WILDCARD) // NOTE: required to support "non-standard" variants
@Produces(MediaType.WILDCARD)
public class JacksonXmlBindSmileProvider extends JacksonSmileProvider
{
    /**
     * Default annotation sets to use, if not explicitly defined during
     * construction: use Jackson annotations if found; if not, use
     * XmlBind annotations as fallback.
     */
    public final static Annotations[] DEFAULT_ANNOTATIONS = {
        Annotations.JACKSON, Annotations.JAKARTA_XML_BIND
    };

    /**
     * Default constructor, usually used when provider is automatically
     * configured to be used with Jakarta-RS implementation.
     */
    public JacksonXmlBindSmileProvider()
    {
        this(null, DEFAULT_ANNOTATIONS);
    }

    /**
     * @param annotationsToUse Annotation set(s) to use for configuring
     * data binding
     */
    public JacksonXmlBindSmileProvider(Annotations... annotationsToUse)
    {
        this(null, annotationsToUse);
    }
    
    /**
     * Constructor to use when a custom mapper (usually components
     * like serializer/deserializer factories that have been configured)
     * is to be used.
     */
    public JacksonXmlBindSmileProvider(ObjectMapper mapper, Annotations[] annotationsToUse)
    {
        super(mapper, annotationsToUse);
    }
}