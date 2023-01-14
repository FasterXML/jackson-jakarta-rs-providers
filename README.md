## Overview

This is a multi-module project that contains Jackson-based Jakarta-RS (*) providers
for following data formats:

* [JSON](https://github.com/FasterXML/jackson-core)
* [Smile](https://github.com/FasterXML/jackson-dataformat-smile) (binary JSON)
* [CBOR](https://github.com/FasterXML/jackson-dataformat-cbor) (another kind of binary JSON)
* [XML](https://github.com/FasterXML/jackson-dataformat-xml)
* [YAML](https://github.com/FasterXML/jackson-dataformat-yaml)

Providers implement Jakarta-RS `MessageBodyReader` and `MessageBodyWriter` handlers for specific
data formats. They also contain SPI settings for auto-registration.

[![Build Status](https://travis-ci.org/FasterXML/jackson-jakarta-rs-providers.svg?branch=master)](https://travis-ci.org/FasterXML/jackson-jakarta-rs-providers)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.fasterxml.jackson.jakarta.rs/jackson-jakarta-rs-json-provider/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.fasterxml.jackson.jakarta.rs/jackson-jakarta-rs-json-provider/)
[![Javadoc](https://javadoc-emblem.rhcloud.com/doc/com.fasterxml.jackson.jakarta.rs/jackson-jakarta-rs-json-provider/badge.svg)](https://www.javadoc.io/doc/com.fasterxml.jackson.jakarta.rs/jackson-jakarta-rs-json-provider)

(*) NOTE: Jakarta-RS is the package under `jakarta.ws.rs`, replacing older JAX-RS which lived
under `javax.ws.rs`.
For JAX-RS variant, see repo [jackson-jaxrs-providers](../../../jackson-jaxrs-providers)

## Build

Usual Maven build like other Jackson repos, with one difference: requires JDK 11 to build
(as opposed to JDK 8 for most other repos), as of Jackson 2.13

## Status

Module is fully functional and considered mature.

## Maven dependency

To use Jakarta-RS on Maven-based projects, use dependencies like:

```xml
<dependency>
  <groupId>com.fasterxml.jackson.jakarta.rs</groupId>
  <artifactId>jackson-jakarta-rs-json-provider</artifactId>
  <version>2.14.1</version>
</dependency>
```

(above is for JSON provider; modify appropriately for other providers)

## Usage: registering providers

Due to auto-registration, it should be possible to simply add Maven dependency
(or include jar if using other build systems) and let Jakarta-RS implementation discover
provider.
If this does not work you need to consult documentation of the Jakarta-RS implementation for details.  

To use Jackson with Jersey see [their documentation](https://jersey.github.io/documentation/latest/media.html#json.jackson).

### Annotations on resources

In addition to annotation value classes, it is also possible to use a subset
of Jackson annotations with provider.

Here is a short list of supported annotations that work with all formats:

* `@JsonView` can be used to define active view for specific endpoint
* `@JsonRootName` can be used to specify alternate rootname; most often used with XML, but possibly with JSON as well.
* `@JacksonAnnotationsInside` meta-annotation may be used as a marker, to create "annotation bundles", similar to how they are used with value type annotations
* `com.fasterxml.jackson.jakarta.rs.annotation.JacksonFeatures` can be used with all provid to enable/disable
    * `SerializationFeature` / `DeserializationFeature` for data-binding configuration
    * `JsonParser.Feature` / `JsonGenerator.Feature` for low(er) level Streaming read/write options

In addition there are format-specific annotations that may be used:

* JSON has:
    * `com.fasterxml.jackson.jakarta.rs.json.annotation.JSONP` to define `JSONP` wrapping for serialized result

## Support

### Community support

Jackson components are supported by the Jackson community through mailing lists, Gitter forum, Github issues. See [Participation, Contributing](../../../jackson#participation-contributing) for full details.

### Enterprise support

Support may be added via Tidelift Subscription model, if there is enough interest.

-----

## Other

For documentation, downloads links, check out [Wiki](../../wiki)
