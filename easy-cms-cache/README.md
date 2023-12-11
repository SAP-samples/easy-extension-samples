# Easy-CMS-Cache

## Overview
This easy extension aims to improve the CMS Rendering Cache by:
- Caching different type of items: `AbstractCMSComponent`, `ContentSlot` and `AbstractPage`.
- Provide different `RenderingItemCacheKeyProvider` to simplify the cache management for custom CMS types. 

## How to Install
This easy extension can be installed using the [Easy Extension Framework](https://sap.github.io/easy-extension-framework/getting-started/).

## How to Use it
The CMS Rendering Cache can be enabled/disabled using the following property:
```
cms.rendering.cache.enabled=true/false
```
The CMS Rendering Cache can be tuned with the following properties:
```
cms.rendering.region.cache.eviction.policy=LFU
cms.rendering.region.cache.exclusive.computation=false
cms.rendering.region.cache.size=20000
cms.rendering.region.cache.stats.enabled=true
cms.rendering.region.cache.ttl=300
```

The following CMS Item types are automatically cached (if the cache is enabled)

### AbstractCMSComponent
Each CMS Component based on `AbstractCMSComponent` is cached using the following key strategy:
- Item PK - the primary key of the item.
- Modified Time - the time when the item was modified.
- Session Currency ISO - the iso-code of the currency used by the user.
- Session Language ISO - the iso-code of the language used by the user.

An example of cache key is:
```
8796127724592Mon Dec 04 17:22:06 CET 2023USDen
```

### ContentSlot
Each `ContentSlot` is cached using the following key strategy:
- Item PK - the primary key of the item.
- Modified Time - the time when the item was modified.
- Session Currency ISO - the iso-code of the currency used by the user.
- Session Language ISO - the iso-code of the language used by the user.
- Logged vs. Anonymous - the boolean value if the user is logged or anonymous.
- User Group - the primary keys of the customer groups to which the user belongs.

An example of cache key is:
```
8796127724592Mon Dec 04 17:22:06 CET 2023USDenfalse8796093120517
```

### AbstractPage
Each CMS Page based on `AbstractPage` is cached using the following key strategy:
- Item PK - the primary key of the item.
- Modified Time - the time when the item was modified.
- Session Currency ISO - the iso-code of the currency used by the user.
- Session Language ISO - the iso-code of the language used by the user.
- Logged vs. Anonymous - the boolean value if the user is logged or anonymous.
- User Group - the primary keys of the customer groups to which the user belongs.
- Segments - the primary keys of the segments to which the user belongs.

An example of cache key is:
```
8796127724592Mon Dec 04 17:22:06 CET 2023USDenfalse87960931205178796095155735
```

## How Does it Works
This easy extension provide several `RenderingItemCacheKeyProvider` for generating ad-hoc cache key based on the requirements of the specific item type.

### CommonI18NRenderingItemCacheKeyProvider
The [CommonI18NRenderingItemCacheKeyProvider](src/main/groovy/com/sap/cx/boosters/easy/extension/cmscache/provider/CommonI18NRenderingItemCacheKeyProvider.groovy) cache key generation strategy is based on the following attributes:
- Item PK - the primary key of the item.
- Modified Time - the time when the item was modified.
- Session Currency ISO - the iso-code of the currency used by the user.
- Session Language ISO - the iso-code of the language used by the user.

This kind of provider is suitable for cache entry that should consider language & currency of the user.

### IsAnonymousRenderingItemCacheKeyProvider
The [IsAnonymousRenderingItemCacheKeyProvider](src/main/groovy/com/sap/cx/boosters/easy/extension/cmscache/provider/IsAnonymousRenderingItemCacheKeyProvider.groovy) cache key generation strategy is based on the following attributes:
- Item PK - the primary key of the item.
- Modified Time - the time when the item was modified.
- Session Currency ISO - the iso-code of the currency used by the user.
- Session Language ISO - the iso-code of the language used by the user.
- Logged vs. Anonymous - the boolean value if the user is logged or anonymous.

This kind of provider is suitable for cache entry that should consider also the status - logged or anonymous - of the user (e.g. CMS Restriction).

### UserGroupRenderingItemCacheKeyProvider
The [UserGroupRenderingItemCacheKeyProvider](src/main/groovy/com/sap/cx/boosters/easy/extension/cmscache/provider/UserGroupRenderingItemCacheKeyProvider.groovy) cache key generation strategy is based on the following attributes:
- Item PK - the primary key of the item.
- Modified Time - the time when the item was modified.
- Session Currency ISO - the iso-code of the currency used by the user.
- Session Language ISO - the iso-code of the language used by the user.
- Logged vs. Anonymous - the boolean value if the user is logged or anonymous.
- User Group - the primary keys of the customer groups to which the user belongs.

This kind of provider is suitable for cache entry that should consider also the user's groups (e.g. CMSGroup Restrictions)

### SegmentRenderingItemCacheKeyProvider
The [SegmentRenderingItemCacheKeyProvider](src/main/groovy/com/sap/cx/boosters/easy/extension/cmscache/provider/SegmentRenderingItemCacheKeyProvider.groovy) cache key generation strategy is based on the following attributes:
- Item PK - the primary key of the item.
- Modified Time - the time when the item was modified.
- Session Currency ISO - the iso-code of the currency used by the user.
- Session Language ISO - the iso-code of the language used by the user.
- Logged vs. Anonymous - the boolean value if the user is logged or anonymous.
- User Group - the primary keys of the customer groups to which the user belongs.
- Segments - the primary keys of the segments to which the user belongs.

This kind of provider is suitable for cache entry that should consider also the segments of the user (e.g. in case of personalization).

## How to Extends with Custom CMS Type
The file [EasyBeans.groovy](src/main/groovy/EasyBeans.groovy) is used to define the required `RenderingItemCacheKeyProvider` for the specific CMS-Type:
```
// Define the "RenderingItemCacheKeyProvider" bean.
abstractCMSComponentRenderingItemCacheKeyProvider(CommonI18NRenderingItemCacheKeyProvider)

// Map the "RenderingItemCacheKeyProvider" for a specific type.
spring.getBean('cmsRenderingCacheKeyProviders')["AbstractCMSComponent"] = spring.getBean('abstractCMSComponentRenderingItemCacheKeyProvider')
```
The following bean are provided by the extensions and can be re-used:
- Language & Currency - [CommonI18NRenderingItemCacheKeyProvider](src/main/groovy/com/sap/cx/boosters/easy/extension/cmscache/provider/CommonI18NRenderingItemCacheKeyProvider.groovy)
- Logged vs. Anonymous - [IsAnonymousRenderingItemCacheKeyProvider](src/main/groovy/com/sap/cx/boosters/easy/extension/cmscache/provider/IsAnonymousRenderingItemCacheKeyProvider.groovy)
- User Group - [UserGroupRenderingItemCacheKeyProvider](src/main/groovy/com/sap/cx/boosters/easy/extension/cmscache/provider/UserGroupRenderingItemCacheKeyProvider.groovy)
- Personalization Segment - [SegmentRenderingItemCacheKeyProvider](src/main/groovy/com/sap/cx/boosters/easy/extension/cmscache/provider/SegmentRenderingItemCacheKeyProvider.groovy)

## How to Troubleshoot cache
This groovy script can be used to analyze the CMS-Rendering cache:
```
def dumpEntries = true
def limit = 100

def cacheController = spring.parent.getBean('cacheController') as de.hybris.platform.regioncache.CacheController

cacheController.regions.findAll{it.name == 'cmsRenderingCacheRegion'}.each{region ->
    println "region: ${region}"
    if (region instanceof de.hybris.platform.regioncache.region.impl.EHCacheRegion) {
        println "nativeCache: ${region.cacheMap}"
        region.cacheMap.store.map.segments.each{segment ->
            def entries = segment.table.findAll{it}
            if (entries) {
                println "  ${segment} [${entries.size()}]"
                if (dumpEntries) {
                    segment.table.findAll{it}.take(limit).each{
                        println "      key: ${it.key.key}"
                        def element = it.value as net.sf.ehcache.Element
                        element.with{
                            println "      eternal: ${eternal}"
                            println "      lifespanSet: ${lifespanSet}"
                            println "      cacheDefaultLifespan: ${cacheDefaultLifespan}"
                            println "      timeToIdle: ${timeToIdle}"
                            println "      timeToLive: ${timeToLive}"
                            println "      currentTime: ${currentTime}"
                            println "      expirationTime: ${expirationTime}"

                        }
                    }
                }
            }
        }
    }
}

return
```
