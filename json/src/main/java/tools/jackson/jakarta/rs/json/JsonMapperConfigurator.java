package tools.jackson.jakarta.rs.json;

import tools.jackson.databind.*;
import tools.jackson.databind.cfg.MapperBuilder;

import tools.jackson.databind.json.JsonMapper;
import tools.jackson.jakarta.rs.cfg.MapperConfiguratorBase;

/**
 * Helper class used to encapsulate details of configuring an
 * {@link ObjectMapper} instance to be used for data binding, as
 * well as accessing it.
 */
public class JsonMapperConfigurator
    extends MapperConfiguratorBase<JsonMapperConfigurator, JsonMapper>
{
    public JsonMapperConfigurator(JsonMapper mapper,
            AnnotationIntrospector aiOverride)
    {
        super(mapper, aiOverride);
    }

    /*
    /**********************************************************************
    /* Abstract method impls
    /**********************************************************************
     */

    @Override
    protected MapperBuilder<?,?> mapperBuilder() {
        return JsonMapper.builder();
    }
}
