package tools.jackson.jakarta.rs.smile;

import java.lang.annotation.Annotation;

import tools.jackson.databind.*;
import tools.jackson.databind.cfg.MapperConfig;
import tools.jackson.jakarta.rs.cfg.EndpointConfigBase;

/**
 * Container class for figuring out annotation-based configuration
 * for Jakarta-RS end points.
 */
public class SmileEndpointConfig
    extends EndpointConfigBase<SmileEndpointConfig>
{
    /*
    /**********************************************************
    /* Construction
    /**********************************************************
     */

    protected SmileEndpointConfig(MapperConfig<?> config) {
        super(config);
    }

    public static SmileEndpointConfig forReading(ObjectReader reader,
            Annotation[] annotations)
    {
        return new SmileEndpointConfig(reader.getConfig())
            .add(annotations, false)
            .initReader(reader)
        ;
    }

    public static SmileEndpointConfig forWriting(ObjectWriter writer,
            Annotation[] annotations)
    {
        return new SmileEndpointConfig(writer.getConfig())
            .add(annotations, true)
            .initWriter(writer)
        ;
    }

    // 09-Jul-2015, tatu: Nothing to do here, for 2.6
    /*
    @Override
    protected void addAnnotation(Class<? extends Annotation> type,
            Annotation annotation, boolean forWriting)
    {
        super.addAnnotation(type, annotation, forWriting);
    }    
    */
    
    @Override
    public Object modifyBeforeWrite(Object value) {
        // nothing to add
        return value;
    }
}
