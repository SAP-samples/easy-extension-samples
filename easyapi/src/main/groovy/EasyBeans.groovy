import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.hybris.platform.core.Registry;
import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader;
import com.sap.cx.boosters.easy.core.api.controllers.*;

Logger LOG = LoggerFactory.getLogger("easyrest");
LOG.info('Registering Spring beans...');

def applicationContext = Registry.getCoreApplicationContext();

GroovyBeanDefinitionReader reader = new GroovyBeanDefinitionReader(applicationContext.getBeanFactory())

reader.beans {
	swaggerController(SwaggerController) {
		easyRestService = applicationContext.getBean("easyRestService")
	}
	getRepositoriesController(GetRepositoriesController) {
		easyRepositoryService = applicationContext.getBean("easyRepositoryService")
	}	
	updateRepositoryController(UpdateRepositoryController) {
		easyInstaller = applicationContext.getBean("easyInstaller")
	}
	getExtensionsController(GetExtensionsController) {
		easyRepositoryService = applicationContext.getBean("easyRepositoryService")
	}
	installExtensionController(InstallExtensionController) {
		easyInstaller = applicationContext.getBean("easyInstaller")
	}
	reinstallExtensionController(ReInstallExtensionController) {
		easyInstaller = applicationContext.getBean("easyInstaller")
	}
	uninstallExtensionController(UninstallExtensionController) {
		easyInstaller = applicationContext.getBean("easyInstaller")
	}
	getEasyEventStatusController(GetEasyEventStatusController) {
		easyEventLogger = applicationContext.getBean("easyEventLogger")
	}
}

LOG.info('Spring beans registered');