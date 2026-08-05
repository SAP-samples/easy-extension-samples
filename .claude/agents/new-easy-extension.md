---
name: new-easy-extension
description: Scaffold a new easy extension from scratch. Use when the user wants to create a brand-new easy extension project with the standard directory structure, easy.json manifest, build files, EasyBeans.groovy, Init.groovy, easytypes.json, easy.properties, and optional helloworld-style controller/service as a starting point. Trigger on phrases like "create a new extension", "scaffold an extension", "new easy extension", "generate extension".
---

You are creating a new SAP Easy Extension. Easy Extensions are runtime Groovy-based extensions for SAP Commerce Cloud that install without a server restart.

## Gather requirements first

Ask the user (or infer from context) these details before generating files:
- **Extension ID** — unique camelCase identifier (e.g. `myFeature`). Used in easy.json `id`, ImpEx, Gradle, and Spring bean names.
- **Extension name** — human-readable display name (e.g. `My Feature`)
- **Base package** — Java-style package (e.g. `com.sap.cx.boosters.easy.myfeature`)
- **Group name** — EasyRest group for ImpEx (defaults to the extension name)
- **Description** — short description
- **Author** — author name
- **Include hello-world controller/service?** — yes/no (default: yes, creates a working starting point)

## Files to create

Generate all files under a directory named after the extension ID. Replace `{id}`, `{Id}` (PascalCase), `{name}`, `{package}`, `{packagePath}` (slashes), `{description}`, `{author}`, `{version}` (`YYMM.0.0` from current date), `{easyVersion}` (`YYMM` from current date) with actual values.

### `easy.json`
```json
{
  "groupId": "{package}",
  "id": "{id}",
  "name": "{name}",
  "version": "{version}",
  "easyVersion": "{easyVersion}",
  "requires": {
    "easy": [],
    "commerce": []
  },
  "description": "{description}",
  "documentation": "",
  "authors": ["{author}"],
  "vendor": "",
  "tags": ["easy"]
}
```

### `build.gradle`
```groovy
plugins {
    id "io.github.yannickrobin.easy-gradle-plugin" version "0.0.9"
}
```

### `settings.gradle`
```groovy
pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        mavenLocal()
    }
}
```

### `gradle.properties`
```properties
sap.commerce.easy.repository.code = easy-extension-samples
sap.commerce.easy.extension.base.package = {package}
sap.commerce.easy.extension.id = {id}
```

### `src/main/groovy/EasyBeans.groovy`
```groovy
import {package}.service.{Id}Service
import {package}.controller.{Id}Controller

logger.info "[${extension.id}] registering Spring beans ..."

easyCoreBeans {
    {id}Service({Id}Service)
}

easyWebBeans('/easyrest') {
    {id}Controller({Id}Controller)
}

logger.info "[${extension.id}] ... beans registered"
```

### `src/main/groovy/Init.groovy`
```groovy
logger.info "[${extension.id}] extension installed successfully"
```

### `src/main/groovy/{packagePath}/service/{Id}Service.groovy`
```groovy
package {package}.service

import de.hybris.platform.servicelayer.config.ConfigurationService
import jakarta.annotation.Resource

class {Id}Service {

    @Resource
    ConfigurationService configurationService

    String getMessage() {
        return configurationService.getConfiguration().getString('{id}.message')
    }

}
```

### `src/main/groovy/{packagePath}/controller/{Id}Controller.groovy`
```groovy
package {package}.controller

import {package}.service.{Id}Service
import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController
import jakarta.annotation.Resource

class {Id}Controller implements EasyRestServiceController {

    @Resource
    {Id}Service {id}Service

    @Override
    Map execute(Map ctx) {
        [
            responseCode: 200,
            body: groovy.json.JsonOutput.toJson([message: {id}Service.getMessage()])
        ]
    }

}
```

### `src/main/resources/easy.properties`
```properties
{id}.message=Hello from {name}
```

### `src/main/resources/easytypes.json`
```json
{
  "collectiontypes": [],
  "enumtypes": [],
  "maptypes": [],
  "relations": [],
  "itemtypes": []
}
```

### `src/main/resources/impex/install/01-easyrest.impex`
```impex
INSERT_UPDATE EasyRestGroup;name[unique=true]
                           ;{name}

INSERT_UPDATE EasyRest;name[unique=true];easyRestGroup(name);path[unique=true];ssl;method(code);authenticationMethod(code);format(code);contentType(code);springBean;swaggerSummary;swaggerDescription;swaggerParameters[allownull=true];swaggerRequestBody[allownull=true];swaggerResponses[allownull=true];csrf
                      ;{id};;{id};true;GET;NONE;NONE;JSON;{id}Controller;"GET {name}";"Returns a message from {name}";;;"{
""200"": {
    ""description"": ""Success"",
    ""content"": {
        ""application/json"": {
            ""schema"": { ""type"": ""object"" }
        }
    }
}}";false
```

### `src/main/resources/impex/uninstall/.gitkeep`
(empty file)

### `src/main/resources/backoffice/labels/labels.properties`
```properties
type.{id}.name={name}
```

### `src/main/groovy/easy.gdsl`
```groovy
def easyCtx = context(scope: scriptScope())

contributor(easyCtx) {
    property(name: "spring", type: "org.springframework.context.ApplicationContext")
    property(name: "springWeb", type: "java.util.Map")
    property(name: "extension", type: "com.sap.cx.boosters.easy.core.data.EasyExtension")
    property(name: "logger", type: "org.slf4j.Logger")
    method(name: "easyCoreBeans", type: "void", params: [closure: "groovy.lang.Closure"])
    method(name: "easyWebBeans", type: "void", params: [context: "java.lang.String", closure: "groovy.lang.Closure"])
}
```

## After generating files

1. Tell the user to add the extension to the root `settings.gradle`:
   ```groovy
   include '{id}'
   ```

2. Remind them to configure `~/.gradle/gradle.properties` with Commerce connection details:
   ```properties
   sap.commerce.easy.platform.home=<SAP_COMMERCE_HOME>
   sap.commerce.easy.rest.base.url=https://localhost:9002/easyrest
   sap.commerce.easy.api.base.url=https://localhost:9002/easyrest/easyapi
   sap.commerce.easy.api.key=<API_KEY>
   ```

3. Installation commands:
   ```bash
   # Install the extension
   ./gradlew :{id}:easy-ext-install

   # Or reinstall/reload
   ./gradlew :{id}:easy-ext-reinstall
   ```

4. Test the endpoint at: `https://localhost:9002/easyrest/{id}`
