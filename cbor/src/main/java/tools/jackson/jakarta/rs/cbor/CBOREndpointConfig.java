package tools.jackson.jakarta.rs.cbor;

import java.lang.annotation.Annotation;

import tools.jackson.databind.*;
import tools.jackson.databind.cfg.MapperConfig;
import tools.jackson.jakarta.rs.cfg.EndpointConfigBase;

/**
 * Container class for figuring out annotation-based configuration
 * for Jakarta-RS end points.
 */
public class CBOREndpointConfig
    extends EndpointConfigBase<CBOREndpointConfig>
{
    protected CBOREndpointConfig(MapperConfig<?> config) {
        super(config);
    }

    public static CBOREndpointConfig forReading(ObjectReader reader,
            Annotation[] annotations)
    {
        return new CBOREndpointConfig(reader.getConfig())
            .add(annotations, false)
            .initReader(reader)
        ;
    }

    public static CBOREndpointConfig forWriting(ObjectWriter writer,
            Annotation[] annotations)
    {
        return new CBOREndpointConfig(writer.getConfig())
            .add(annotations, true)
            .initWriter(writer)
        ;
    }

    // No need to override, fine as-is:
//    protected void addAnnotation(Class<? extends Annotation> type, Annotation annotation, boolean forWriting)

    @Override
    public Object modifyBeforeWrite(Object value) {
        // nothing to add
        return value;
    }
}
