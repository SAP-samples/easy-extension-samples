import com.sap.cx.boosters.easy.delivery.controller.AvailableSlotsController
import com.sap.cx.boosters.easy.delivery.controller.BookDeliveryController
import com.sap.cx.boosters.easy.delivery.controller.CancelDeliveryController
import com.sap.cx.boosters.easy.delivery.controller.ChangeDeliveryController
import com.sap.cx.boosters.easy.delivery.controller.ConfirmDeliveryController
import com.sap.cx.boosters.easy.delivery.controller.GetBookedDeliveryController
import de.hybris.platform.core.Registry
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.DefaultListableBeanFactory

LOG = LoggerFactory.getLogger("easy-tutorial-step6")
def applicationName = spring.getApplicationName()
LOG.info("Registering Spring beans for ${applicationName}...")

beanFactory = (DefaultListableBeanFactory) Registry.getCoreApplicationContext().getBeanFactory()
def reader = new GroovyBeanDefinitionReader(beanFactory)

println "Registering beans on Core Application Context: " + Registry.getCoreApplicationContext()

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
	homeDeliveryCommercePlaceOrderMethodHook(com.sap.cx.boosters.easy.delivery.placeorderhook.HomeDeliveryCommercePlaceOrderMethodHook){
		deliverySlotService = spring.getBean("deliverySlotService")
	}
	deliverySlotManagementAttributeHandler(com.sap.cx.boosters.easy.delivery.handler.DeliverySlotManagementAttributeHandler){
		flexibleSearchService = spring.getBean('flexibleSearchService')
	}
	homeDeliveryModeOrderPopulator(com.sap.cx.boosters.easy.delivery.populators.HomeDeliveryModeOrderPopulator){
	}
}

println "Adding the CommercePlaceOrderHook: "
def List commercePlaceOrderMethodHooks = spring.getBean("commercePlaceOrderMethodHooks")
def homeDeliveryCommercePlaceOrderMethodHook = spring.getBean("homeDeliveryCommercePlaceOrderMethodHook")
commercePlaceOrderMethodHooks.removeAll { it.getClass().getSimpleName().equalsIgnoreCase("HomeDeliveryCommercePlaceOrderMethodHook")}
commercePlaceOrderMethodHooks.add(homeDeliveryCommercePlaceOrderMethodHook)
println "Modifiyng order populator for the home delivery mode: "
def homeDeliveryModeOrderPopulator = spring.getBean("homeDeliveryModeOrderPopulator")
def orderConverter = spring.getBean("orderConverter")
orderConverter.getPopulators().removeAll { it.getClass().getSimpleName().equalsIgnoreCase("HomeDeliveryModeOrderPopulator")}
orderConverter.getPopulators().add(homeDeliveryModeOrderPopulator)


println "Registering beans on Web Application Context: " + spring

reader = new GroovyBeanDefinitionReader(spring.beanFactory as BeanDefinitionRegistry)
if (applicationName == '/easyrest') {
	reader.beans {
		availableSlotsController(AvailableSlotsController) {
			deliverySlotService = spring.getBean("deliverySlotService")
			warehouseService = spring.getBean("warehouseService")
			defaultCartGenericDao = spring.getBean("defaultCartGenericDao")
			configurationService = spring.getBean("configurationService")
		}
		bookDeliveryController(BookDeliveryController) {
			deliverySlotService = spring.getBean("deliverySlotService")
			defaultCartGenericDao = spring.getBean("defaultCartGenericDao")
		}
		cancelDeliveryController(CancelDeliveryController) {
			deliverySlotService = spring.getBean("deliverySlotService")
			defaultCartGenericDao = spring.getBean("defaultCartGenericDao")
		}
		confirmDeliveryController(ConfirmDeliveryController) {
			deliverySlotService = spring.getBean("deliverySlotService")
			commerceOrderDao = spring.getBean("commerceOrderDao")
		}
		getBookedDeliveryController(GetBookedDeliveryController) {
			deliverySlotService = spring.getBean("deliverySlotService")
			defaultCartGenericDao = spring.getBean("defaultCartGenericDao")
		}
		changeDeliveryController(ChangeDeliveryController) {
			deliverySlotService = spring.getBean("deliverySlotService")
			defaultCartGenericDao = spring.getBean("defaultCartGenericDao")
		}
	}
}


LOG.info("Spring beans registered for ${applicationName}")
