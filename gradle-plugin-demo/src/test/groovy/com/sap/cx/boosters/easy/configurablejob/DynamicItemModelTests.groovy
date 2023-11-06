package com.sap.cx.boosters.easy.configurablejob

import com.sap.cx.boosters.easy.configurablecronjob.easytype.models.ConfigurableCronJobModel
import com.sap.cx.boosters.easy.configurablejob.models.DummyDynamicModel
import com.sap.cx.boosters.easy.configurablejob.models.DynamicConfigurableCronjobModel
import com.sap.cx.boosters.easytype.ItemModelAttribute
import de.hybris.platform.servicelayer.model.AbstractItemModel
import de.hybris.platform.servicelayer.model.DefaultNewModelContextFactory
import org.springframework.util.ReflectionUtils
import spock.lang.Specification

class DynamicItemModelTests extends Specification {

    def "list fields"() {

        when:
        def fields = ReflectionUtils.getDeclaredFields(DynamicConfigurableCronjobModel).findAll{it.isAnnotationPresent(ItemModelAttribute)}
        fields.each{
            println it
        }

        then: true

    }

    def "dump metaClass"() {

        when:
        def model = new DynamicConfigurableCronjobModel()

        println 'metaClass.methods'
        model.getMetaClass().methods.each {
            println "  $it"
        }

        println 'metaClass.metaMethods'
        model.getMetaClass().metaMethods.each {
            println "  $it"
        }

        println 'metaClass.properties'
        model.getMetaClass().properties.each {
            if (it instanceof MetaBeanProperty && it.field) {
                println "  [${it.getClass().name}] ${it.field.declaringClass.name} ${it.name}"
            } else {
                println "  [${it.getClass().name}] ${it.name}"
            }
        }

        then: true

    }

    def "metaClass"() {

        when:
        def fields = ReflectionUtils.getDeclaredFields(DynamicConfigurableCronjobModel).findAll{it.isAnnotationPresent(ItemModelAttribute)}
        fields.each{
            println it
        }

        def filter = {MetaProperty it -> it instanceof MetaBeanProperty && it.field && fields*.name.contains(it.field.name)}

        DynamicConfigurableCronjobModel.metaClass.properties.findAll(filter).each{
            def metaBeanProperty = it as MetaBeanProperty
            println "${metaBeanProperty.name}"
            println "  ${metaBeanProperty.getter.name}"
            println "  ${metaBeanProperty.setter.name}"
        }

        then: true

    }

    def "set and read fields - dotted notation"() {

        when:
        def code = '0000ABCD'
        def params = [param1:'param1Value']
        def maxDuration = 10
        def model = new DynamicConfigurableCronjobModel()

        model.code = code
        model.params = params
        model.maxDuration = maxDuration


        then:
        model.code == code && model.params == params && model.maxDuration == maxDuration && model.@params == null && model.@maxDuration == null

    }

    def "set and read fields - accessor methods"() {

        when:
        def code = '0000ABCD'
        def params = [param1:'param1Value']
        def maxDuration = 10
        def model = new DynamicConfigurableCronjobModel()

        model.setCode(code)
        model.setParams(params)
        model.setMaxDuration(maxDuration)

        then:
        model.getCode() == code && model.getParams() == params && model.getMaxDuration() == maxDuration && model.@params == null && model.@maxDuration == null

    }

    def "set and read fields - no trait - dotted notation"() {

        when:
        def code = '0000ABCD'
        def now = new Date()
        def model = new DummyDynamicModel()
        model.code = code
        model.creationtime = now

        then:
        model.code == code && model.creationtime == now && model.@code == null

    }

    def "set and read fields - no trait - accessor methods"() {

        when:
        def code = '0000ABCD'
        def now = new Date()
        def model = new DummyDynamicModel()
        model.setCode(code)
        model.setCreationtime(now)

        then:
        model.getCode() == code && model.getCreationtime() == now && model.@code == null

    }

    def "missing property"() {

        when:
        def model = new ConfigurableCronJobModel()
        model.missingProperty

        then:
        def exception = thrown(RuntimeException)
        exception.message == "Undefined attribute missingProperty for ${model.getClass().name}"

    }

    def "missing accessor method"() {

        when:
        def model = new ConfigurableCronJobModel()
        model.getMissingProperty()

        then:
        thrown(MissingMethodException)

    }

    def "dump itemModelContext properties"() {

        when:
        // de.hybris.platform.servicelayer.model.DefaultNewModelContextFactory
        def modelContextFactory = AbstractItemModel.EntityContextFactoryHolder.factory as DefaultNewModelContextFactory
        def itemModelContext = modelContextFactory.createNew(ConfigurableCronJobModel)
        println itemModelContext
        itemModelContext.properties.each {p -> println p}

        then:
        itemModelContext

    }

    def "check typecode"() {

        when:
        def typeCode = ConfigurableCronJobModel.simpleName - 'Model'

        then:
        ConfigurableCronJobModel._TYPECODE == typeCode

    }

}
