Here are people who have contributed to development Jackson JSON processor
Jakarta-RS providers module, version 2.x
(version numbers in brackets indicate release in which the problem was fixed)

Tatu Saloranta, tatu.saloranta@iki.fi: author

PJ Fanning (@pjfanning)

* Contributed #12: Remove unnecessary synchronization from endpoint
  reader/writer caches
 (2.14.2)
* Contributed #26: Replace synchronized blocks with `ReentrantLock`s
 (2.17.1)
* Contributed #28: Mark variables as volatile for safe concurrent access
 (2.17.1)

Steven Schlansker (@stevenschlansker)

* Contributed #16: Add `JakartaRsFeature.READ_FULL_STREAM` to consume all content, on by default
 (2.15.0)

Kevin Wooten (@kdubb)

* Suggested #36: Narrow types to format specific (e.g. CBORMapper) when resolving
  via JAX-RS Providers	
 (2.19.0)
