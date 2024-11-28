Project: jackson-jakarta-rs-providers
Sub-modules:
  jackson-jakarta-rs-cbor-provider
  jackson-jakarta-rs-json-provider
  jackson-jakarta-rs-smile-provider
  jackson-jakarta-rs-xml-provider
  jackson-jakarta-rs-yaml-provider

------------------------------------------------------------------------
=== Releases ===
------------------------------------------------------------------------

2.18.2 (27-Nov-2024)
2.18.1 (28-Oct-2024)

No changes since 2.18.0

2.18.0 (26-Sep-2024)

#30: `JacksonJaxbJsonProvider` has @Produces(MediaType.WILDCARD) but
  hasMatchingMediaType(MediaType.WILDCARD) return false
* Woodstox dependency now 7.0.0

2.17.3 (01-Nov-2024)

No changes since 2.17.2

2.17.2 (05-Jul-2024)

* Woodstox dependency now 6.7.0

2.17.1 (04-May-2024)

#26: Replace synchronized blocks with `ReentrantLock`s
 (contributed by @pjfanning)
#28: Mark variables as volatile for safe concurrent access
 (contributed by @pjfanning)

2.17.0 (12-Mar-2024)

* Upgrade Woodstox to 6.6.1

2.16.2 (09-Mar-2024)

No changes since 2.16.1

2.16.1 (24-Dec-2023)

#23: Deprecate local `LRUMap`, use `jackson-databind` provided one instead

2.16.0 (15-Nov-2023)

No changes since 2.15

2.15.4 (15-Feb-2024)
2.15.3 (12-Oct-2023)
2.15.2 (30-May-2023)
2.15.1 (16-May-2023)

No changes since 2.15.0

2.15.0 (23-Apr-2023)

#16: Add `JakartaRsFeature.READ_FULL_STREAM` to consume all content, on by default
 (contributed by Steven S)
* Upgrade Woodstox to 6.5.1

2.14.3 (05-May-2023)

No changes since 2.14.2

2.14.2 (28-Jan-2023)

#12: Remove unnecessary synchronization from endpoint reader/writer caches
 (contributed by @pjfanning)
* Upgrade Woodstox to 6.5.0 for a fix to OSGi metadata

2.14.1 (21-Nov-2022)

No changes since 2.14.0

2.14.0 (05-Nov-2022)

* (xml) Woodstox dependency -> 6.4.0

2.13.4 (03-Sep-2022)

* (xml) Woodstox dependency -> 6.3.1

2.13.3 (14-May-2022)
2.13.2 (06-Mar-2022)
2.13.1 (19-Dec-2021)

No changes since 2.13.1

2.13.0 (30-Sep-2021)

* First publicly released version!
