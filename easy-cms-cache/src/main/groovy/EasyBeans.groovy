import com.sap.cx.boosters.easy.extension.cmscache.DebugRenderingCacheKeyProvider
import com.sap.cx.boosters.easy.extension.cmscache.provider.IsAnonymousRenderingItemCacheKeyProvider
import com.sap.cx.boosters.easy.extension.cmscache.provider.SegmentRenderingItemCacheKeyProvider
import com.sap.cx.boosters.easy.extension.cmscache.provider.UserGroupRenderingItemCacheKeyProvider
import com.sap.cx.boosters.easy.extension.cmscache.provider.CommonI18NRenderingItemCacheKeyProvider

logger.info "[${extension.id}] registering Spring beans ..."

easyCoreBeans {

    /** Re-Usable Providers
     * Language & Currency - commonI18NRenderingItemCacheKeyProvider(CommonI18NRenderingItemCacheKeyProvider)
     * Logged vs. Anon - isAnonymousRenderingItemCacheKeyProvider(IsAnonymousRenderingItemCacheKeyProvider)
     * User Group - userGroupRenderingItemCacheKeyProvider(UserGroupRenderingItemCacheKeyProvider)
     * Segment - segmentRenderingItemCacheKeyProvider(SegmentRenderingItemCacheKeyProvider)
     */

    /** CMS-Type: AbstractCMSComponent */
    abstractCMSComponentRenderingItemCacheKeyProvider(CommonI18NRenderingItemCacheKeyProvider)
    cmsRenderingCacheKeyProviders["AbstractCMSComponent"] = abstractCMSComponentRenderingItemCacheKeyProvider

    /** CMS-Type: AbstractPage */
    abstractPageRenderingItemCacheKeyProvider(SegmentRenderingItemCacheKeyProvider)
    cmsRenderingCacheKeyProviders["AbstractPage"] = abstractPageRenderingItemCacheKeyProvider

    /** CMS-Type: ContentSlot */
    contentSlotRenderingItemCacheKeyProvider(UserGroupRenderingItemCacheKeyProvider)
    cmsRenderingCacheKeyProviders["ContentSlot"] = contentSlotRenderingItemCacheKeyProvider

}

logger.info "[${extension.id}] finished Spring beans registration "
