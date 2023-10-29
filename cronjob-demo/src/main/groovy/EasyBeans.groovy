import com.sap.cx.boosters.easy.cronjobdemo.job.DemoJobPerformable
import de.hybris.platform.core.Registry
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader

LOG = LoggerFactory.getLogger('easy-cronjob-demo')
LOG.info('Registering Spring beans...')

def applicationContext = Registry.getCoreApplicationContext();

GroovyBeanDefinitionReader reader = new GroovyBeanDefinitionReader(applicationContext.getBeanFactory())

reader.beans {
    demoJobPerformable(DemoJobPerformable) {
        modelService = spring.getBean('modelService')
        sessionService = spring.getBean('sessionService')
        flexibleSearchService = spring.getBean('flexibleSearchService')
    }
}

LOG.info('Spring beans registered')
