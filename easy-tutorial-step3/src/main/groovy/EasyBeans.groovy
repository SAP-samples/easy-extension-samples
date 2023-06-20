import de.hybris.platform.core.Registry
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader
import org.springframework.beans.factory.support.BeanDefinitionRegistry
import org.springframework.beans.factory.support.DefaultListableBeanFactory

LOG = LoggerFactory.getLogger("easy-tutorial-step3")
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
}



println "Registering beans on Web Application Context: " + spring

reader = new GroovyBeanDefinitionReader(spring.beanFactory as BeanDefinitionRegistry)
if (applicationName == '/easyrest') {
	reader.beans {
	}
}


LOG.info("Spring beans registered for ${applicationName}")
