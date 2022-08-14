package tools.jackson.jakarta.rs.cfg;

import jakarta.ws.rs.core.MultivaluedMap;

import tools.jackson.core.*;
import tools.jackson.databind.*;

public abstract class ObjectWriterModifier
{
    /**
     * Method called to let modifier make any changes it wants to to objects
     * used for writing response for specified endpoint.
     * 
     * @param responseHeaders HTTP headers being returned with response (mutable)
     */
    public abstract ObjectWriter modify(EndpointConfigBase<?> endpoint,
            MultivaluedMap<String,Object> responseHeaders,
            Object valueToWrite, ObjectWriter w)
        throws JacksonException;
}
