package com.sap.cx.boosters.easy.extension.cmscache

import de.hybris.platform.cmsfacades.rendering.cache.impl.DefaultRenderingCacheKeyProvider
import de.hybris.platform.core.model.ItemModel
import de.hybris.platform.regioncache.key.CacheKey
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class DebugRenderingCacheKeyProvider extends DefaultRenderingCacheKeyProvider {

    private static final Logger LOG = LoggerFactory.getLogger(DebugRenderingCacheKeyProvider.class);

    @Override
    Optional<CacheKey> getKey(final ItemModel item)
    {
        println("DebugRenderingCacheKeyProvider");
        Optional<CacheKey> cacheKey = super.getKey(item)
        if(cacheKey.isEmpty()) {
            LOG.debug("cms-rendering-cache missing provider for item: {}/{}", item.getPk().getLongValueAsString(), item.getItemtype())
        }
        return cacheKey;
    }

}
