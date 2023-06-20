import com.sap.cx.boosters.easy.delivery.controller.*
import de.hybris.platform.core.Registry
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.DefaultListableBeanFactory

LOG = LoggerFactory.getLogger("easy-tutorial-step5")
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
	defaultCartGenericDao(de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao, 'Cart') {
		flexibleSearchService = spring.getBean('flexibleSearchService')
	}
}

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
