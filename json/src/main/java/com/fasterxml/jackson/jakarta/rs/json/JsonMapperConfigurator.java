package com.fasterxml.jackson.jakarta.rs.json;

import java.util.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;

import com.fasterxml.jackson.module.jakarta.xmlbind.JakartaXmlBindAnnotationIntrospector;

import com.fasterxml.jackson.jakarta.rs.cfg.Annotations;
import com.fasterxml.jackson.jakarta.rs.cfg.MapperConfiguratorBase;

/**
 * Helper class used to encapsulate details of configuring an
 * {@link ObjectMapper} instance to be used for data binding, as
 * well as accessing it.
 */
public class JsonMapperConfigurator
    extends MapperConfiguratorBase<JsonMapperConfigurator, ObjectMapper>
{
    public JsonMapperConfigurator(ObjectMapper mapper, Annotations[] defAnnotations)
    {
        super(mapper, defAnnotations);
    }

    /**
     * Method that locates, configures and returns {@link ObjectMapper} to use
     */
    @Override
    public synchronized ObjectMapper getConfiguredMapper() {
        /* important: should NOT call mapper(); needs to return null
         * if no instance has been passed or constructed
         */
        return _mapper;
    }

    @Override
    public synchronized ObjectMapper getDefaultMapper() {
        if (_defaultMapper == null) {
            _defaultMapper = new ObjectMapper();
            _setAnnotations(_defaultMapper, _defaultAnnotationsToUse);
        }
        return _defaultMapper;
    }

    /*
    /**********************************************************
    /* Abstract method impl
    /**********************************************************
     */

    /**
     * Helper method that will ensure that there is a configurable non-default
     * mapper (constructing an instance if one didn't yet exit), and return
     * that mapper.
     */
    @Override
    protected ObjectMapper mapper()
    {
        if (_mapper == null) {
            _mapper = new ObjectMapper();
            _setAnnotations(_mapper, _defaultAnnotationsToUse);
        }
        return _mapper;
    }

    /*
    /**********************************************************
    /* Internal methods
    /**********************************************************
     */

    @Override
    protected AnnotationIntrospector _resolveIntrospectors(Annotations[] annotationsToUse)
    {
        // Let's ensure there are no dups there first, filter out nulls
        ArrayList<AnnotationIntrospector> intr = new ArrayList<AnnotationIntrospector>();
        for (Annotations a : annotationsToUse) {
            if (a != null) {
                intr.add(_resolveIntrospector(a));
            }
        }
        int count = intr.size();
        if (count == 0) {
            return AnnotationIntrospector.nopInstance();
        }
        AnnotationIntrospector curr = intr.get(0);
        for (int i = 1, len = intr.size(); i < len; ++i) {
            curr = AnnotationIntrospector.pair(curr, intr.get(i));
        }
        return curr;
    }

    protected AnnotationIntrospector _resolveIntrospector(Annotations ann)
    {
        switch (ann) {
        case JACKSON:
            return new JacksonAnnotationIntrospector();
        case JAKARTA_XML_BIND:
            // For this, need to use indirection just so that error occurs
            // when we get here, and not when this class is being loaded
            if (_xmlBindIntrospectorClass == null) {
                try {
                    _xmlBindIntrospectorClass = JakartaXmlBindAnnotationIntrospector.class;
                    return _xmlBindIntrospectorClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new IllegalStateException("Failed to instantiate JakartaXmlBindAnnotationIntrospector: "+e.getMessage(), e);
                }
            }
        default:
            throw new IllegalStateException(); 
        }
    }
}
