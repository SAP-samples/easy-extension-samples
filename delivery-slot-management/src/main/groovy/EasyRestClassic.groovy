// import de.hybris.platform.converters.impl.AbstractPopulatingConverter
import de.hybris.platform.converters.impl.AbstractPopulatingConverter
import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.context.ConfigurableApplicationContext

import static com.sap.cx.boosters.easy.core.extension.task.processor.impl.install.EasyBeanDefinitionReader.*
import de.hybris.platform.core.Registry
import org.slf4j.LoggerFactory
// import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader
// import org.springframework.beans.factory.support.BeanDefinitionRegistry
// import org.springframework.context.ConfigurableApplicationContext

def LOG = LoggerFactory.getLogger('easy-deliverySlotDemo')
LOG.info ">>> Registering Spring beans for ${spring.applicationName}... AEI"

def beanFactory = ((ConfigurableApplicationContext) Registry.coreApplicationContext).beanFactory
def reader = new GroovyBeanDefinitionReader(beanFactory)

println "Registering beans on Core Application Context: ${Registry.coreApplicationContext}"

reader.beans {

    deliverySlotService(com.sap.cx.boosters.easy.delivery.service.DeliverySlotService) {
        flexibleSearchService = spring.getBean('flexibleSearchService')
        modelService = spring.getBean('modelService')
        enumerationService = spring.getBean('enumerationService')
    }

    deliverySlotCleanupService(com.sap.cx.boosters.easy.delivery.service.DeliverySlotCleanupService) {
        flexibleSearchService = spring.getBean('flexibleSearchService')
        modelService = spring.getBean('modelService')
    }

    defaultCartGenericDao(de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao, 'Cart') {
        flexibleSearchService = spring.getBean('flexibleSearchService')
    }

    homeDeliveryCommercePlaceOrderMethodHook(com.sap.cx.boosters.easy.delivery.placeorderhook.HomeDeliveryCommercePlaceOrderMethodHook) {
        deliverySlotService = spring.getBean('deliverySlotService')
    }

    deliverySlotManagementAttributeHandler(com.sap.cx.boosters.easy.delivery.handler.DeliverySlotManagementAttributeHandler) {
        flexibleSearchService = spring.getBean('flexibleSearchService')
    }

    homeDeliveryModeOrderPopulator(com.sap.cx.boosters.easy.delivery.populators.HomeDeliveryModeOrderPopulator) {
    }

}

println 'Adding the CommercePlaceOrderHook:'
def commercePlaceOrderMethodHooks = spring.getBean('commercePlaceOrderMethodHooks') as List
def homeDeliveryCommercePlaceOrderMethodHook = spring.getBean('homeDeliveryCommercePlaceOrderMethodHook')
commercePlaceOrderMethodHooks.removeAll { it.getClass().simpleName.equalsIgnoreCase('HomeDeliveryCommercePlaceOrderMethodHook')}
commercePlaceOrderMethodHooks.add(homeDeliveryCommercePlaceOrderMethodHook)

println 'Modifying order populator for the home delivery mode:'
def homeDeliveryModeOrderPopulator = spring.getBean('homeDeliveryModeOrderPopulator')
def orderConverter = spring.getBean('orderConverter') as AbstractPopulatingConverter
orderConverter.populators.removeAll { it.getClass().simpleName.equalsIgnoreCase('HomeDeliveryModeOrderPopulator')}
orderConverter.populators.add(homeDeliveryModeOrderPopulator)

println "Registering beans on Web Application Context: $spring"

reader = new GroovyBeanDefinitionReader(spring.beanFactory as BeanDefinitionRegistry)

if (spring.applicationName == '/easyrest') {

    reader.beans {

        availableSlotsController(com.sap.cx.boosters.easy.delivery.controller.AvailableSlotsController) {
            deliverySlotService = spring.getBean('deliverySlotService')
            warehouseService = spring.getBean('warehouseService')
            defaultCartGenericDao = spring.getBean('defaultCartGenericDao')
            configurationService = spring.getBean('configurationService')
        }

        bookDeliveryController(com.sap.cx.boosters.easy.delivery.controller.BookDeliveryController) {
            deliverySlotService = spring.getBean('deliverySlotService')
            defaultCartGenericDao = spring.getBean('defaultCartGenericDao')
        }

        cancelDeliveryController(com.sap.cx.boosters.easy.delivery.controller.CancelDeliveryController) {
            deliverySlotService = spring.getBean('deliverySlotService')
            defaultCartGenericDao = spring.getBean('defaultCartGenericDao')
        }

        confirmDeliveryController(com.sap.cx.boosters.easy.delivery.controller.ConfirmDeliveryController) {
            deliverySlotService = spring.getBean('deliverySlotService')
            commerceOrderDao = spring.getBean('commerceOrderDao')
        }

        getBookedDeliveryController(com.sap.cx.boosters.easy.delivery.controller.GetBookedDeliveryController) {
            deliverySlotService = spring.getBean('deliverySlotService')
            defaultCartGenericDao = spring.getBean('defaultCartGenericDao')
        }

        changeDeliveryController(com.sap.cx.boosters.easy.delivery.controller.ChangeDeliveryController) {
            deliverySlotService = spring.getBean('deliverySlotService')
            defaultCartGenericDao = spring.getBean('defaultCartGenericDao')
        }

    }

}

LOG.info "Spring beans registered for ${spring.applicationName}"