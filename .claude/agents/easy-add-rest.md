---
name: easy-add-rest
description: Add a new REST endpoint to an existing easy extension. Creates the controller class, registers it in EasyBeans.groovy, and adds the EasyRest ImpEx entry. Use when the user wants to expose a new HTTP endpoint from their extension. Trigger on phrases like "add REST endpoint", "add controller", "create endpoint", "add API endpoint", "new route".
---

You are adding a new REST endpoint to an existing SAP Easy Extension.

## How Easy REST works

- **Controller** — a Groovy class implementing `EasyRestServiceController` with a single `Map execute(Map ctx)` method
- **`EasyBeans.groovy`** — registers the controller bean in the `/easyrest` web context
- **ImpEx `EasyRest` entry** — declares the endpoint: URL path, HTTP method, auth, and which Spring bean handles it
- **`ctx` map** contains: `ctx.parameters` (query params), `ctx.pathParameters` (path variables like `{id}`), `ctx.headers`, `ctx.body` (parsed JSON for POST/PUT/PATCH)

## Gather requirements

Ask the user (or infer from context):
- **Extension ID and base package** — read from `easy.json` and `gradle.properties`
- **Controller name** — e.g. `GetOrdersController` (PascalCase)
- **Spring bean name** — e.g. `getOrdersController` (camelCase, used in ImpEx `springBean`)
- **HTTP method** — GET, POST, PUT, PATCH, DELETE
- **URL path** — e.g. `orders/{orderId}` (path params in `{braces}`)
- **Authentication** — NONE, OAUTH, BASIC, JWT (default: NONE)
- **EasyRest group name** — look in existing ImpEx files for the group already defined
- **What the endpoint should do** — to generate a meaningful implementation

## Step 1 — Create the controller class

File: `src/main/groovy/{packagePath}/controller/{ControllerName}.groovy`

```groovy
package {package}.controller

import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController
// Add @Resource imports for any services needed

class {ControllerName} implements EasyRestServiceController {

    // @Resource
    // {ServiceType} {serviceName}

    @Override
    Map execute(Map ctx) {
        // ctx.parameters       — query string / form params (Map)
        // ctx.pathParameters   — path variables e.g. ctx.pathParameters.orderId
        // ctx.headers          — HTTP request headers
        // ctx.body             — request body (parsed JSON for POST/PUT/PATCH)

        try {
            def result = [:] // TODO: implement logic

            return [
                responseCode: 200,
                body: groovy.json.JsonOutput.toJson(result)
            ]
        } catch (Exception e) {
            return [
                responseCode: 500,
                body: groovy.json.JsonOutput.toJson([error: e.message])
            ]
        }
    }

}
```

## Step 2 — Register in EasyBeans.groovy

Add the import at the top of `src/main/groovy/EasyBeans.groovy`:
```groovy
import {package}.controller.{ControllerName}
```

Add the bean inside the `easyWebBeans('/easyrest')` closure:
```groovy
easyWebBeans('/easyrest') {
    // ... existing beans ...
    {beanName}({ControllerName})
}
```

## Step 3 — Add ImpEx entry

Append to (or create) `src/main/resources/impex/install/0N-easyrest.impex`:

```impex
INSERT_UPDATE EasyRest;name[unique=true];easyRestGroup(name);path[unique=true];ssl;method(code);authenticationMethod(code);format(code);contentType(code);springBean;swaggerSummary;swaggerDescription;swaggerParameters[allownull=true];swaggerRequestBody[allownull=true];swaggerResponses[allownull=true];csrf
                      ;{endpointName};{groupName};{urlPath};true;{METHOD};{AUTH};NONE;JSON;{beanName};"{swagger summary}";"{swagger description}";"{swaggerParameters}";"{swaggerRequestBody}";"{swaggerResponses}";false
```

### Swagger parameters template (path + query params)
```json
[
  {
    ""in"": ""path"",
    ""name"": ""paramName"",
    ""description"": ""Description"",
    ""required"": true,
    ""type"": ""string""
  },
  {
    ""in"": ""query"",
    ""name"": ""queryParam"",
    ""description"": ""Description"",
    ""required"": false,
    ""type"": ""string""
  }
]
```

### Swagger request body template (POST/PUT/PATCH)
```json
{
  ""description"": ""Request body"",
  ""required"": true,
  ""content"": {
    ""application/json"": {
      ""schema"": {
        ""type"": ""object"",
        ""properties"": {
          ""fieldName"": { ""type"": ""string"" }
        }
      }
    }
  }
}
```

### Swagger responses template
```json
{
  ""200"": {
    ""description"": ""Success"",
    ""content"": {
      ""application/json"": {
        ""schema"": { ""type"": ""object"" }
      }
    }
  },
  ""404"": { ""description"": ""Not found"" },
  ""500"": { ""description"": ""Internal server error"" }
}
```

## Step 4 — Reinstall the extension

```bash
./gradlew :{extensionId}:easy-ext-reinstall
```

The new endpoint is immediately available at:
`https://localhost:9002/easyrest/{urlPath}`

Swagger docs: `https://localhost:9002/easyrest/swagger-ui/`

## Important notes

- Path segment separators use `/` in the ImpEx path — no leading slash
- Double-quote all embedded JSON in ImpEx using `""` (two double-quotes = one literal `"`)
- `format(code)` is the **request** format (NONE for no body, JSON, XML, FORM_URL_ENCODED)
- `contentType(code)` is the **response** content type (JSON, XML, TEXT, HTML)
- `authenticationMethod(code)` values: NONE, OAUTH, BASIC, JWT, API_KEY
