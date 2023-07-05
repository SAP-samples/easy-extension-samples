import com.sap.cx.boosters.easy.deliverybtp.controller.*
import com.sap.cx.boosters.easy.deliverybtp.service.DeliverySlotCleanupServiceBtp
import de.hybris.platform.core.Registry
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.DefaultListableBeanFactory

LOG = LoggerFactory.getLogger("easy-deliverySlotDemo")
def applicationName = spring.getApplicationName()
LOG.info("Registering Spring beans for ${applicationName}...")

beanFactory = (DefaultListableBeanFactory) Registry.getCoreApplicationContext().getBeanFactory()
def reader = new GroovyBeanDefinitionReader(beanFactory)

println "Registering beans on Core Application Context: " + Registry.getCoreApplicationContext()

reader.beans {
	defaultCartGenericDao(de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao, 'Cart') {
		flexibleSearchService = spring.getBean('flexibleSearchService')
	}
	deliverySlotCleanupServiceBtp(DeliverySlotCleanupServiceBtp) {
		flexibleSearchService = spring.getBean('flexibleSearchService')
		modelService = spring.getBean('modelService')
	}
	homeDeliveryCommercePlaceOrderMethodHookBtp(com.sap.cx.boosters.easy.deliverybtp.placeorderhook.HomeDeliveryCommercePlaceOrderMethodHookBtp){
		destinationService = spring.getBean("destinationService")
		integrationRestTemplateFactory = spring.getBean("integrationRestTemplateFactory")
	}
	homeDeliveryModeOrderPopulatorBtp(com.sap.cx.boosters.easy.deliverybtp.populators.HomeDeliveryModeOrderPopulatorBtp){
		destinationService = spring.getBean("destinationService")
		integrationRestTemplateFactory = spring.getBean("integrationRestTemplateFactory")
	}
}

println "Adding the CommercePlaceOrderHook: "
def List commercePlaceOrderMethodHooks = spring.getBean("commercePlaceOrderMethodHooks")
def homeDeliveryCommercePlaceOrderMethodHookBtp = spring.getBean("homeDeliveryCommercePlaceOrderMethodHookBtp")
commercePlaceOrderMethodHooks.removeAll { it.getClass().getSimpleName().equalsIgnoreCase("HomeDeliveryCommercePlaceOrderMethodHookBtp")}
commercePlaceOrderMethodHooks.add(homeDeliveryCommercePlaceOrderMethodHookBtp)
println "Modifiyng order populator for the home delivery mode: "
def homeDeliveryModeOrderPopulatorBtp = spring.getBean("homeDeliveryModeOrderPopulatorBtp")
def orderConverter = spring.getBean("orderConverter")
orderConverter.getPopulators().removeAll { it.getClass().getSimpleName().equalsIgnoreCase("HomeDeliveryModeOrderPopulatorBtp")}
orderConverter.getPopulators().add(homeDeliveryModeOrderPopulatorBtp)

println "Registering beans on Web Application Context: " + spring

reader = new GroovyBeanDefinitionReader(spring.beanFactory as BeanDefinitionRegistry)
if (applicationName == '/easyrest') {
	reader.beans {
		availableSlotsControllerBtp(AvailableSlotsControllerBtp) {
			warehouseService = spring.getBean("warehouseService")
			defaultCartGenericDao = spring.getBean("defaultCartGenericDao")
			configurationService = spring.getBean("configurationService")
			destinationService = spring.getBean("destinationService")
			integrationRestTemplateFactory = spring.getBean("integrationRestTemplateFactory")
		}
		bookDeliveryControllerBtp(BookDeliveryControllerBtp) {
			defaultCartGenericDao = spring.getBean("defaultCartGenericDao")
			destinationService = spring.getBean("destinationService")
			integrationRestTemplateFactory = spring.getBean("integrationRestTemplateFactory")
		}
		getBookedDeliveryControllerBtp(GetBookedDeliveryControllerBtp){
			defaultCartGenericDao = spring.getBean("defaultCartGenericDao")
			destinationService = spring.getBean("destinationService")
			integrationRestTemplateFactory = spring.getBean("integrationRestTemplateFactory")
		}
		changeDeliveryControllerBtp(ChangeDeliveryControllerBtp){
			defaultCartGenericDao = spring.getBean("defaultCartGenericDao")
			destinationService = spring.getBean("destinationService")
			integrationRestTemplateFactory = spring.getBean("integrationRestTemplateFactory")
		}
		confirmDeliveryControllerBtp(ConfirmDeliveryControllerBtp){
			defaultCartGenericDao = spring.getBean("defaultCartGenericDao")
			destinationService = spring.getBean("destinationService")
			integrationRestTemplateFactory = spring.getBean("integrationRestTemplateFactory")
			commerceOrderDao = spring.getBean("commerceOrderDao")
		}
		cancelDeliveryControllerBtp(CancelDeliveryControllerBtp){
			defaultCartGenericDao = spring.getBean("defaultCartGenericDao")
			destinationService = spring.getBean("destinationService")
			integrationRestTemplateFactory = spring.getBean("integrationRestTemplateFactory")
		}
	}
}


LOG.info("Spring beans registered for ${applicationName}")
