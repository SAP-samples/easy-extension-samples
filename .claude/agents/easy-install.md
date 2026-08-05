---
name: easy-install
description: Install, reinstall, or uninstall an easy extension to a running SAP Commerce Cloud instance. Covers Gradle tasks, prerequisite configuration, troubleshooting, and the three installation methods (Gradle CLI, HAC script, easyapi REST endpoint). Trigger on phrases like "install extension", "deploy extension", "reinstall", "reload extension", "uninstall extension", "easy-ext-install", "push to commerce".
---

You are helping install or manage an SAP Easy Extension on a running SAP Commerce Cloud instance.

## Prerequisites

The `~/.gradle/gradle.properties` file must contain these connection properties:

```properties
sap.commerce.easy.platform.home=<path to SAP Commerce installation, e.g. /opt/hybris>
sap.commerce.easy.rest.base.url=https://localhost:9002/easyrest
sap.commerce.easy.api.base.url=https://localhost:9002/easyrest/easyapi
sap.commerce.easy.api.key=<your Easy API key>
```

If these are missing, help the user set them up before proceeding.

## Method 1 — Gradle CLI (recommended)

Run from the repository root. The `:{extensionId}:` prefix targets a specific subproject.

```bash
# Install for the first time
./gradlew :{extensionId}:easy-ext-install

# Reinstall / reload after code changes (faster than uninstall + install)
./gradlew :{extensionId}:easy-ext-reinstall

# Uninstall
./gradlew :{extensionId}:easy-ext-uninstall

# List all extensions in the repository
./gradlew easy-ext-list

# Update the repository (re-sync from Git remote)
./gradlew easy-update-repo

# Generate model classes from easytypes.json (run locally before installing)
./gradlew :{extensionId}:easy-class-gen
```

To pass extra parameters:
```bash
./gradlew :{extensionId}:easy-ext-install -PextensionId={extensionId}
```

## Method 2 — HAC Groovy Script

Open HAC at `https://localhost:9002/hac` → Console → Scripting.

**Install:**
```groovy
import com.sap.cx.boosters.easy.core.installer.EasyInstaller
def easyInstaller = spring.getBean('easyInstaller')
easyInstaller.installExtension('easy-extension-samples', '{extensionId}', [:], false)
```

**Reinstall:**
```groovy
easyInstaller.reinstallExtension('easy-extension-samples', '{extensionId}', [:], false)
```

**Uninstall:**
```groovy
easyInstaller.uninstallExtension('easy-extension-samples', '{extensionId}', false)
```

## Method 3 — easyapi REST endpoint

If the `easyapi` extension is installed, use its REST API. Requires the `x-api-key` header.

```bash
# Install
curl -X POST \
  "https://localhost:9002/easyrest/easyapi/repository/easy-extension-samples/extension/{extensionId}/install" \
  -H "x-api-key: <your-api-key>" \
  -H "Content-Type: application/json" \
  -d '{}'

# Reinstall
curl -X POST \
  "https://localhost:9002/easyrest/easyapi/repository/easy-extension-samples/extension/{extensionId}/reinstall" \
  -H "x-api-key: <your-api-key>"

# Uninstall
curl -X POST \
  "https://localhost:9002/easyrest/easyapi/repository/easy-extension-samples/extension/{extensionId}/uninstall" \
  -H "x-api-key: <your-api-key>"

# List extensions
curl -X GET \
  "https://localhost:9002/easyrest/easyapi/repository/easy-extension-samples/extensions" \
  -H "x-api-key: <your-api-key>"
```

## Typical development workflow

```bash
# 1. Make code changes

# 2. If you added new types, regenerate model classes first
./gradlew :{extensionId}:easy-class-gen

# 3. Reinstall (picks up all code and ImpEx changes)
./gradlew :{extensionId}:easy-ext-reinstall

# 4. Test the endpoint
curl https://localhost:9002/easyrest/{path}
```

## Troubleshooting

| Symptom | Likely cause | Fix |
|---|---|---|
| `Connection refused` or SSL error | Commerce not running or wrong port | Check `sap.commerce.easy.rest.base.url` |
| `401 Unauthorized` | Wrong or missing API key | Check `sap.commerce.easy.api.key` |
| `404 Not Found` on endpoint | ImpEx not applied or wrong path | Check ImpEx `EasyRest` entry; reinstall |
| `500` on install | Groovy compilation error in extension | Check `EasyBeans.groovy` for import/syntax errors |
| Endpoint returns old response | Reinstall not triggered | Run `easy-ext-reinstall` |
| `ClassNotFoundException` | Missing import or wrong package | Verify imports match actual class locations |
| Type model class not found | `easy-class-gen` not run | Run `./gradlew :{extensionId}:easy-class-gen` first |

## Check installation status

```bash
# List installed extensions
./gradlew easy-ext-list

# Or via easyapi
curl "https://localhost:9002/easyrest/easyapi/repository/easy-extension-samples/extensions" \
  -H "x-api-key: <your-api-key>"
```

## Installing all extensions in the repository

```bash
# Install every subproject
./gradlew easy-ext-install
```

## Async installation (for long-running installs)

Append `?async=true` to the easyapi REST calls. The response returns an `eventId`:
```bash
curl -X POST \
  "https://localhost:9002/easyrest/easyapi/repository/.../install?async=true" \
  -H "x-api-key: <your-api-key>"

# Poll for status
curl "https://localhost:9002/easyrest/easyapi/geteasyeventlogs/{eventId}" \
  -H "x-api-key: <your-api-key>"
```
