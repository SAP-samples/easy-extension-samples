---
name: easy-add-type
description: Add a new item type, enumeration, or attribute to an existing easy extension's easytypes.json. Handles new item types with deployment config, enumerations, and extending existing Commerce types with new attributes. Also generates the model class registration in easy.properties. Trigger on phrases like "add item type", "create type", "add enum", "add attribute", "extend type", "add type to data model", "easytypes".
---

You are adding a type definition to an SAP Easy Extension's data model.

Easy Type is the runtime equivalent of SAP Commerce's `items.xml`. Definitions go in `src/main/resources/easytypes.json`. After modifying this file, run `./gradlew easy-class-gen` to generate model classes into `gensrc/`.

## Type categories

- **`itemtypes`** — new business entity types (equivalent to `<itemtype>` in items.xml)
- **`enumtypes`** — fixed-value enumerations
- **`collectiontypes`** — typed collections
- **`maptypes`** — key-value map types
- **`relations`** — many-to-many or one-to-many relations

## Gather requirements

Ask the user (or infer from context):
- **Type kind** — item type, enum, attribute extension to existing type?
- **Type code** — PascalCase unique name (e.g. `DeliverySlot`)
- **Table name** — DB table for new item types (e.g. `deliveryslots`)
- **Typecode** — unique 5-digit integer (e.g. `26001`). Check existing types to avoid collision; use a range >= 25000 for custom types
- **Model class name** — typically `{TypeCode}Model` (e.g. `DeliverySlotModel`)
- **Super type** — parent type (default: `GenericItem`)
- **Attributes** — list of attribute names, their types, and modifiers
- **Base package** — from `gradle.properties`, used to build the `typeClass` fully qualified name

## New item type template

Add to the `itemtypes` array in `easytypes.json`:

```json
{
  "code": "MyType",
  "name": [{ "lang": "en", "value": "My Type" }],
  "autocreate": "true",
  "generate": "true",
  "superType": "GenericItem",
  "modelClassName": "MyTypeModel",
  "deployment": {
    "table": "mytypes",
    "typecode": "26XXX",
    "propstable": "mytypeprops"
  },
  "attributes": [
    {
      "qualifier": "code",
      "type": "java.lang.String",
      "name": [{ "lang": "en", "value": "Code" }],
      "persistence": { "type": "property", "column": "p_code" },
      "modifiers": {
        "unique": "true",
        "initial": "true",
        "optional": "false",
        "write": "false",
        "partof": "false"
      }
    }
  ]
}
```

## Common attribute types

| Type string | Java type | Notes |
|---|---|---|
| `java.lang.String` | String | |
| `java.lang.Integer` | Integer | |
| `java.lang.Double` | Double | |
| `java.lang.Boolean` | Boolean | |
| `java.util.Date` | Date | |
| `localized:java.lang.String` | String | Localized text |
| `{CommerceType}` | Commerce model | Use `typeClass` for the FQN |
| `{EasyType}` | Easy model | Use `typeClass` for the FQN |

For references to other types, add `typeClass` with the fully qualified model class name:
```json
{
  "qualifier": "relatedItem",
  "type": "MyOtherType",
  "typeClass": "{package}.models.MyOtherTypeModel",
  ...
}
```

## Attribute modifiers reference

| Modifier | Values | Meaning |
|---|---|---|
| `unique` | true/false | Part of uniqueness key |
| `initial` | true/false | Can be set only on creation |
| `optional` | true/false | Nullable |
| `write` | true/false | Mutable after creation |
| `partof` | true/false | Cascade delete with owner |

## Enumeration template

Add to the `enumtypes` array:

```json
{
  "code": "MyStatus",
  "name": [{ "lang": "en", "value": "My Status" }],
  "values": [
    { "code": "ACTIVE", "name": [{ "lang": "en", "value": "Active" }] },
    { "code": "INACTIVE", "name": [{ "lang": "en", "value": "Inactive" }] }
  ]
}
```

## Extending an existing Commerce type (attribute extension)

To add attributes to an existing type like `BaseStore`, `Cart`, `Product`, etc., add an item type entry **without** `deployment`, `autocreate`, or `generate`:

```json
{
  "code": "BaseStore",
  "autocreate": "false",
  "generate": "false",
  "attributes": [
    {
      "qualifier": "myNewAttribute",
      "type": "java.lang.String",
      "name": [{ "lang": "en", "value": "My New Attribute" }],
      "persistence": { "type": "property", "column": "p_mynewattribute" },
      "modifiers": {
        "unique": "false",
        "initial": "false",
        "optional": "true",
        "write": "true",
        "partof": "false"
      }
    }
  ]
}
```

## After editing easytypes.json

1. **Generate model classes:**
   ```bash
   ./gradlew :{extensionId}:easy-class-gen
   ```
   This creates Groovy model/enum classes in `gensrc/main/groovy/`.

2. **Register generated models in `easy.properties`** — add a line per new type:
   ```properties
   easy.type.{extensionId}.models={package}.models.MyTypeModel,{package}.enums.MyStatus
   ```

3. **Register the model class in `EasyBeans.groovy`** — for custom types, ensure the `easy.properties` key is loaded by the framework (it is picked up automatically if the key follows the convention above).

4. **Reinstall the extension:**
   ```bash
   ./gradlew :{extensionId}:easy-ext-reinstall
   ```

## Typecode allocation advice

- SAP Commerce reserves typecodes 0–10000
- Custom extensions should use 25000–32767
- **Check all `easytypes.json` files in this repo** before choosing a typecode to avoid conflicts:
  ```bash
  grep -r "typecode" */src/main/resources/easytypes.json
  ```
