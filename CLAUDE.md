# CLAUDE.md

This file provides guidance to Claude Code (https://claude.ai/code) when working with code in this repository.

## What this is

Jackson-based Jakarta-RS (`jakarta.ws.rs`) providers: `MessageBodyReader` / `MessageBodyWriter`
implementations for JSON, XML, YAML, CBOR and Smile, plus SPI auto-registration.
Sibling repo `jackson-jaxrs-providers` is the same code for the older `javax.ws.rs` API.

Branches: `3.x` is main development (Jackson 3, `tools.jackson.*` packages, Java 17 baseline);
`2.x` is maintenance (Jackson 2, `com.fasterxml.jackson.*`). Feature branches are usually
`tatu/<version>/<issue-slug>` and merge upward (e.g. `3.2` → `3.x`).

The parent POM (`tools.jackson:jackson-base`) is a SNAPSHOT, resolved from the Sonatype
snapshot repositories declared in the root `pom.xml`, so builds need network access unless
matching Jackson artifacts are in the local repo.

## Build & test

```bash
./mvnw clean verify                 # full build: compile, unit tests, ServiceLoader ITs
./mvnw -pl json -am test            # one module (-am builds `base` first — always needed)
./mvnw -pl json -am test -Dtest=TestJsonView -Dsurefire.failIfNoSpecifiedTests=false
```

- JDK 17+ required (source/target 17).
- `-Dtest=X` with `-am` fails on the `base` module unless
  `-Dsurefire.failIfNoSpecifiedTests=false` is added.
- `*IT.java` (the `ServiceLoaderIT` in each format module) runs under failsafe during
  `verify`, not `test`. These deliberately run off the classpath (`useModulePath=false`)
  to exercise `META-INF/services`.
- Tests are JUnit 5; end-to-end tests spin up real Jersey-on-Jetty endpoints
  (`.../dw/ResourceTestBase`).

### JPMS gotcha

Every module has `src/main/java/module-info.java` *and* `src/test/java/module-info.java`
declaring the **same** module name (tests are not split out). Test compilation happens on
the module path, so a new test package will not be visible to JUnit until it is added as an
`opens` clause in the test `module-info.java`, and a new test dependency needs a `requires`.

## Architecture

`base` holds all shared logic; each format module (`json`, `xml`, `yaml`, `cbor`, `smile`)
is a thin, near-identical instantiation of it. Changes to provider behaviour almost always
belong in `base`, then get mirrored across formats if format-specific.

`ProviderBase<THIS, MAPPER, EP_CONFIG, MAPPER_CONFIG>` (base module, ~950 lines) is the
self-typed generic implementing both `MessageBodyReader` and `MessageBodyWriter`. Format
modules supply four things:

| Piece | Example (JSON) | Role |
| --- | --- | --- |
| Provider | `JacksonJsonProvider` | implements `hasMatchingMediaType()`, `_locateMapperViaProvider()`, `_configFor{Reading,Writing}()`; carries the `@Provider`/`@Consumes`/`@Produces` annotations |
| XmlBind variant | `JacksonXmlBindJsonProvider` | same provider preconfigured with `JakartaXmlBindAnnotationIntrospector`; class annotations are **duplicated** from the parent and must be kept in sync |
| Endpoint config | `JsonEndpointConfig` | extends `EndpointConfigBase`, builds the per-endpoint `ObjectReader`/`ObjectWriter` |
| Mapper configurator | `JsonMapperConfigurator` | extends `MapperConfiguratorBase`, just returns the format's `MapperBuilder` |

Key mechanisms in `base`:

- **Endpoint config caching** — resolved reader/writer configs are cached in a
  `LookupCache` keyed by `AnnotationBundleKey` (the endpoint's annotation array plus type).
  `AnnotationBundleKey` uses identity comparison of the annotation array for the fast path;
  `immutableKey()` is called before storing. Toggled by `CACHE_ENDPOINT_{READERS,WRITERS}`.
- **Annotation handling** — `EndpointConfigBase.addAnnotation()` is the single place
  `@JsonView`, `@JsonRootName`, `@JacksonFeatures` and `@JacksonAnnotationsInside` bundles
  are interpreted. Format-specific annotations (e.g. `@JSONP`) are handled by the format's
  `EndpointConfig` override.
- **Feature flags** — `JakartaRSFeature` (`ALLOW_EMPTY_INPUT`, `READ_FULL_STREAM`,
  `ADD_NO_SNIFF_HEADER`, `DYNAMIC_OBJECT_MAPPER_LOOKUP`, `MATCH_ALL_IF_NO_MEDIA_TYPE`, the
  cache flags), held as an int bitmask on the provider.
- **Untouchables** — `DEFAULT_UNTOUCHABLES` / `DEFAULT_UNREADABLES` / `DEFAULT_UNWRITABLES`
  keep the provider from claiming `String`, `byte[]`, streams, `Response`, etc.;
  `addUntouchable()`/`removeUntouchable()` override per instance.
- **Per-request overrides** — `ObjectReaderInjector` / `ObjectWriterInjector` are
  `ThreadLocal`-based hooks (set by a JAX-RS filter) letting a request modify the
  reader/writer; `readFrom`/`writeTo` fetch-and-clear them.
- **Mapper lookup** — `locateMapper()` picks between an explicitly set mapper and a
  JAX-RS `ContextResolver`; `DYNAMIC_OBJECT_MAPPER_LOOKUP` reverses the precedence.
- `readFrom` special-cases `JsonParser` and `MappingIterator` return types (the latter
  must be positioned past `START_ARRAY` before `readValues(JsonParser)`).

### SPI registration lives in two places

Both must be updated together when provider class names or packages change (this was the
bug behind issue #57):

1. `<module>/src/main/resources/META-INF/services/jakarta.ws.rs.ext.MessageBody{Reader,Writer}`
2. `provides jakarta.ws.rs.ext.MessageBody{Reader,Writer} with ...` in `module-info.java`

Each module also publishes a `no-metainf-services` classifier jar with the services files
stripped, for containers that must not auto-register.

## Conventions

- Follow the surrounding Jackson style: `/*****/` section banner comments, `_`-prefixed
  non-public fields, and dated inline comments (`// 09-Jul-2015, tatu: ...`) referencing the
  issue being fixed.
- Fixes get an entry in `release-notes/VERSION` and, for outside contributions,
  `release-notes/CREDITS` (2.x equivalents are the `-2.x` files).
