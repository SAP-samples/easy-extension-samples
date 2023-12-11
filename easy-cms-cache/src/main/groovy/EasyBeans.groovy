import de.hybris.platform.core.Registry
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader
import org.springframework.beans.factory.support.DefaultListableBeanFactory

import com.sap.cx.boosters.easy.extension.cmscache.DebugRenderingCacheKeyProvider
import com.sap.cx.boosters.easy.extension.cmscache.provider.IsAnonymousRenderingItemCacheKeyProvider
import com.sap.cx.boosters.easy.extension.cmscache.provider.SegmentRenderingItemCacheKeyProvider
import com.sap.cx.boosters.easy.extension.cmscache.provider.UserGroupRenderingItemCacheKeyProvider
import com.sap.cx.boosters.easy.extension.cmscache.provider.CommonI18NRenderingItemCacheKeyProvider

LOG = LoggerFactory.getLogger("cms-rendering-cache")
LOG.info("registering bean: started");
beanFactory = (DefaultListableBeanFactory) Registry.getCoreApplicationContext().getBeanFactory()
def reader = new GroovyBeanDefinitionReader(beanFactory)

reader.beans {

    /** Re-Usable Providers
     * Language & Currency - commonI18NRenderingItemCacheKeyProvider(CommonI18NRenderingItemCacheKeyProvider)
     * Logged vs. Anon - isAnonymousRenderingItemCacheKeyProvider(IsAnonymousRenderingItemCacheKeyProvider)
     * User Group - userGroupRenderingItemCacheKeyProvider(UserGroupRenderingItemCacheKeyProvider)
     * Segment - segmentRenderingItemCacheKeyProvider(SegmentRenderingItemCacheKeyProvider)
     */

    /** CMS-Type: AbstractCMSComponent */
    abstractCMSComponentRenderingItemCacheKeyProvider(CommonI18NRenderingItemCacheKeyProvider)
    spring.getBean('cmsRenderingCacheKeyProviders')["AbstractCMSComponent"] = spring.getBean('abstractCMSComponentRenderingItemCacheKeyProvider')

    /** CMS-Type: AbstractPage */
    abstractPageRenderingItemCacheKeyProvider(SegmentRenderingItemCacheKeyProvider)
    spring.getBean('cmsRenderingCacheKeyProviders')["AbstractPage"] = spring.getBean('abstractPageRenderingItemCacheKeyProvider')

    /** CMS-Type: ContentSlot */
    contentSlotRenderingItemCacheKeyProvider(UserGroupRenderingItemCacheKeyProvider)
    spring.getBean('cmsRenderingCacheKeyProviders')["ContentSlot"] = spring.getBean('contentSlotRenderingItemCacheKeyProvider')

}

LOG.info("registering bean: finished");