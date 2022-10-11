package tools.jackson.jakarta.rs.smile;

import tools.jackson.databind.*;
import tools.jackson.databind.cfg.MapperBuilder;

import tools.jackson.dataformat.smile.SmileFactory;
import tools.jackson.dataformat.smile.databind.SmileMapper;
import tools.jackson.jakarta.rs.cfg.MapperConfiguratorBase;

/**
 * Helper class used to encapsulate details of configuring an
 * {@link ObjectMapper} instance to be used for data binding, as
 * well as accessing it.
 */
public class SmileMapperConfigurator
    extends MapperConfiguratorBase<SmileMapperConfigurator, SmileMapper>
{
    public SmileMapperConfigurator(SmileMapper mapper,
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
        return SmileMapper.builder(new SmileFactory());
    }
}