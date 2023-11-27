import com.sap.cx.boosters.easy.helloworld.controller.HelloWorldController
import com.sap.cx.boosters.easy.helloworld.service.HelloWorldService

/**
 * 1. binding variables
 *
 *    few binding variables are available in EasyBeans.groovy
 *
 *    - spring:
 *    ---------
 *        this this the spring application context, it corresponds to
 *        de.hybris.platform.core.Registry.coreApplicationContext
 *        or to spring.parent in hac
 *
 *    - springWeb:
 *    ------------
 *        this is a map of spring web contexts
 *        there's no such a binding variable in hac but you can add to Script.metaClass
 *        just execute on hac (once per jvm start): Script.metaClass.springWeb = loadBeansService.springWebContexts
 *
 *    - extension:
 *    ------------
 *        object of type com.sap.cx.boosters.easy.core.data.EasyExtension
 *
 *    - logger:
 *    ---------
 *        corresponding to org.slf4j.LoggerFactory.getLogger("easy.${extension.id}")
 *
 *    - application context beans:
 *    ----------------------------
 *        a spring application context aware binding is used to execute EasyBeans.groovy so bean names are resolved automatically
 *        this is very similar how script execution works on hac
 *          old style: def configurationService = spring.getBean('configurationService')
 *          new style: configurationService
 *
 */

logger.info "[${extension.id}] registering Spring beans for ..."

logger.info "Registering core beans for ${extension.id}"

/**
 *
 *  This corresponds to:
 *  com.sap.cx.boosters.easy.core.spring.beans.reader.impl.DSLEasyBeanDefinitionReaders#easyCoreBeans(groovy.lang.Closure)
 *  a static import is added in compiler options when EasyBeans.groovy is compiled
 *
 *  This corresponds to old style where the BeanDefinitionReader is created in the the EasyBeans.groovy script
 *
 *    def beanFactory = (DefaultListableBeanFactory) Registry.coreApplicationContext.beanFactory
 *    def reader = new GroovyBeanDefinitionReader(beanFactory)
 *    reader.beans {
 *
 *    }
 *
 *  At this stage there's limited support for autocompletion inside this closure
 *  it just delegates to org.springframework.beans.factory.support.DefaultListableBeanFactory
 *
 */
easyCoreBeans {

    /**
     * dependencies of injected with @Resource annotation
     */
    helloWorldService(HelloWorldService)

}

logger.info "Registering web beans for ${extension.id}"

/**
 *
 * def applicationName = spring.applicationName
 * if (applicationName == '/easyrest') {
 *
 * }
 *
 */
easyWebBeans('/easyrest') {
    helloWorldController(HelloWorldController)
}

logger.info "[${extension.id}] ... beans registered"
