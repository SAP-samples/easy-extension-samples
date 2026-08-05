---
name: easy-add-service
description: Add a new service (interface + default implementation) to an existing easy extension and register it in EasyBeans.groovy. Use when the user wants to add business logic encapsulated in a Spring service. Trigger on phrases like "add service", "create service", "new service", "add business logic", "service class".
---

You are adding a new Spring service to an existing SAP Easy Extension.

## How Easy Extension services work

- Services are plain Groovy classes (no interface required, but recommended for testability)
- Dependencies are injected with `@Resource` (JSR-250) by Spring bean name
- They are registered in `EasyBeans.groovy` using the `easyCoreBeans` closure
- Common Commerce services available via `@Resource`: `flexibleSearchService`, `modelService`, `configurationService`, `sessionService`, `userService`, `catalogService`

## Gather requirements

Ask the user (or infer from context):
- **Extension base package** — from `gradle.properties` (`sap.commerce.easy.extension.base.package`)
- **Service name** — PascalCase (e.g. `OrderSummaryService`)
- **Spring bean name** — camelCase (e.g. `orderSummaryService`)
- **What the service does** — to generate a meaningful default implementation
- **Dependencies** — Commerce services or other extension services needed

## Step 1 — Create the service interface (optional but recommended)

File: `src/main/groovy/{packagePath}/service/{ServiceName}.groovy`

```groovy
package {package}.service

interface {ServiceName} {

    /**
     * {Describe what this method does}
     */
    {ReturnType} {methodName}({ParameterType} {paramName})

}
```

## Step 2 — Create the default implementation

File: `src/main/groovy/{packagePath}/service/impl/Default{ServiceName}.groovy`

```groovy
package {package}.service.impl

import {package}.service.{ServiceName}
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import de.hybris.platform.servicelayer.model.ModelService
// Add other imports as needed

import jakarta.annotation.Resource

class Default{ServiceName} implements {ServiceName} {

    @Resource
    FlexibleSearchService flexibleSearchService

    // @Resource
    // ModelService modelService

    @Override
    {ReturnType} {methodName}({ParameterType} {paramName}) {
        // TODO: implement
    }

}
```

### If no interface is needed (simple utility service)

File: `src/main/groovy/{packagePath}/service/{ServiceName}.groovy`

```groovy
package {package}.service

import jakarta.annotation.Resource

class {ServiceName} {

    @Resource
    ConfigurationService configurationService

    {ReturnType} {methodName}({ParameterType} {paramName}) {
        // TODO: implement
    }

}
```

## Step 3 — Register in EasyBeans.groovy

Add the import at the top of `src/main/groovy/EasyBeans.groovy`:
```groovy
import {package}.service.impl.Default{ServiceName}
```

Add to the `easyCoreBeans` closure:
```groovy
easyCoreBeans {
    // ... existing beans ...
    {beanName}(Default{ServiceName})
}
```

If the service has dependencies on other Easy-registered beans, wire them explicitly:
```groovy
easyCoreBeans {
    {beanName}(Default{ServiceName}) { bean ->
        bean.otherService = ref('otherServiceBeanName')
    }
}
```

## Step 4 — Create a unit test (Spock)

File: `src/test/groovy/{packagePath}/service/impl/Default{ServiceName}Test.groovy`

```groovy
package {package}.service.impl

import de.hybris.bootstrap.annotations.UnitTest
import spock.lang.Specification

@UnitTest
class Default{ServiceName}Test extends Specification {

    def service = new Default{ServiceName}()

    def setup() {
        // Mock dependencies
        service.flexibleSearchService = Mock()
    }

    def "test {methodName} with valid input"() {
        given:
        // set up mocks

        when:
        def result = service.{methodName}(input)

        then:
        // assertions
        result != null
    }

}
```

Run tests:
```bash
./gradlew :{extensionId}:test
```

## Common Commerce service injection patterns

```groovy
// Configuration properties
@Resource
ConfigurationService configurationService
// Usage: configurationService.getConfiguration().getString('my.property.key')

// Database queries
@Resource
FlexibleSearchService flexibleSearchService
// Usage: flexibleSearchService.search("SELECT {PK} FROM {MyType} WHERE ...")

// Save/create/delete items
@Resource
ModelService modelService
// Usage: modelService.create(MyTypeModel), modelService.save(item), modelService.remove(item)

// GenericDao — use in EasyBeans: myTypeDao(DefaultGenericDao, MyTypeModel._TYPECODE)
@Resource
GenericDao<MyTypeModel> myTypeDao

// Session / user context
@Resource
SessionService sessionService

@Resource
UserService userService
```

## Step 5 — Reinstall

```bash
./gradlew :{extensionId}:easy-ext-reinstall
```
