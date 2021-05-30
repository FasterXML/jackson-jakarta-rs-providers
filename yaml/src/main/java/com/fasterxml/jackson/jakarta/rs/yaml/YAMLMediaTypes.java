package com.fasterxml.jackson.jakarta.rs.yaml;

import jakarta.ws.rs.core.MediaType;

public class YAMLMediaTypes {
    public static final String    APPLICATION_JACKSON_YAML      = "application/yaml";
    public static final MediaType APPLICATION_JACKSON_YAML_TYPE = MediaType.valueOf(APPLICATION_JACKSON_YAML);
    public static final String    TEXT_JACKSON_YAML      = "text/yaml";
    public static final MediaType TEXT_JACKSON_YAML_TYPE = MediaType.valueOf(TEXT_JACKSON_YAML);
}
