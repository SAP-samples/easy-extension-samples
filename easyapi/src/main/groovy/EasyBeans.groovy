import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import de.hybris.platform.core.Registry;
import org.springframework.beans.factory.groovy.GroovyBeanDefinitionReader;
import com.sap.cx.boosters.easy.core.api.controllers.*;
import com.sap.cx.boosters.easy.core.api.services.*;

Logger LOG = LoggerFactory.getLogger("easyrest");
LOG.info('Registering Spring beans...');

def applicationContext = Registry.getCoreApplicationContext();

GroovyBeanDefinitionReader reader = new GroovyBeanDefinitionReader(applicationContext.getBeanFactory())

reader.beans {
	easyAPIService(EasyAPIService) {
	}
	swaggerController(SwaggerController) {
		easyRestService = applicationContext.getBean("easyRestService")
		configurationService = applicationContext.getBean("configurationService")
	}
	getRepositoriesController(GetRepositoriesController) {
		easyRepositoryService = applicationContext.getBean("easyRepositoryService")
		easyAPIService = applicationContext.getBean("easyAPIService")
	}	
	updateRepositoryController(UpdateRepositoryController) {
		easyInstaller = applicationContext.getBean("easyInstaller")
		easyAPIService = applicationContext.getBean("easyAPIService")
	}
	getExtensionsController(GetExtensionsController) {
		easyRepositoryService = applicationContext.getBean("easyRepositoryService")
		easyAPIService = applicationContext.getBean("easyAPIService")
	}
	installExtensionController(InstallExtensionController) {
		easyInstaller = applicationContext.getBean("easyInstaller")
		easyAPIService = applicationContext.getBean("easyAPIService")
	}
	reinstallExtensionController(ReInstallExtensionController) {
		easyInstaller = applicationContext.getBean("easyInstaller")
		easyAPIService = applicationContext.getBean("easyAPIService")
	}
	uninstallExtensionController(UninstallExtensionController) {
		easyInstaller = applicationContext.getBean("easyInstaller")
		easyAPIService = applicationContext.getBean("easyAPIService")
	}
	getEasyEventStatusController(GetEasyEventStatusController) {
		easyEventLogger = applicationContext.getBean("easyEventLogger")
		easyAPIService = applicationContext.getBean("easyAPIService")
	}
}

LOG.info('Spring beans registered');