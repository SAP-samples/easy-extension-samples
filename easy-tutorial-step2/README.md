# Easy Tutorial Step 2 - How to Extend SAP Commerce Data Model

## Overview
In this step of the tutorial, you will learn how to define a custom data model in an Easy extension, how you can create your model classes for new item types and finally how you can load data through ImpEx files during the extension installation process.

## Home Delivery Data Model
Here's the data model that you will implement for our requirements:

![img.png](./images/img.png)

There are three new item types:
- Vehicle
- DeliverySlot
- DeliverySlotManagement

There is also a new enumeration:
- DeliverySlotStatus

## Creating new item types
Modelling new item types in an Easy extension is a process that requires 2 steps:
- creating the **easytypes.json** file
- creating the model classes for the new item types

### easytypes.json
This file is roughly the equivalent of standard items.xml files where the datamodel of traditional Commerce extensions is defined. The structure of the file, attribute names, etc. everything mirrors the same components in the items.xml files.
Let's see an extract of the file:
```json
"enumtypes": [
  {
    "code": "DeliverySlotStatus",
    "name": [
      {
        "lang": "en",
        "value": "Delivery Slot Status"
      }
    ],
    "values": [
      {
        "code": "BOOKED",
        "name": [
          {
            "lang": "en",
            "value": "Booked"
          }
        ]
      },
      {
        "code": "CONFIRMED",
        "name": [
          {
            "lang": "en",
            "value": "Confirmed"
          }
        ]
      }
    ]
  }
],
"itemtypes": [
  {
    "code": "Vehicle",
    "name": [
      {
        "lang": "en",
        "value": "Vehicle"
      }
    ],
    "autocreate": "true",
    "generate": "true",
    "superType": "GenericItem",
    "modelClassName": "VehicleModel",
    "deployment": {
      "table": "vehicles",
      "typecode": "26000",
      "propstable": "vehicleprops"
    },
    "attributes": [
      {
        "qualifier": "code",
        "type": "java.lang.String",
        "name": [
          {
            "lang": "en",
            "value": "Code"
          }
        ],
        "persistence": {
          "type": "property",
          "column": "p_code"
        },
        "modifiers": {
          "unique": "true",
          "initial": "true",
          "optional": "false",
          "write": "false",
          "partof": "false"
        }
      },
      {
        "qualifier": "name",
        "type": "localized:java.lang.String",
        "name": [
          {
            "lang": "en",
            "value": "Name"
          }
        ],
        "persistence": {
          "type": "property",
          "column": "p_name"
        },
        "modifiers": {
          "unique": "false",
          "initial": "false",
          "optional": "true",
          "write": "true",
          "partof": "false"
        }
      }
    ]
  },
  {
    "code": "DeliverySlot",
    "name": [
      {
        "lang": "en",
        "value": "Delivery Slot"
      }
    ],
    "autocreate": "true",
    "generate": "true",
    "superType": "GenericItem",
    "modelClassName": "DeliverySlotModel",
    "deployment": {
      "table": "deliveryslots",
      "typecode": "26001",
      "propstable": "deliveryslotprops"
    },
    "attributes": [
      {
        "qualifier": "code",
        "type": "java.lang.String",
        "name": [
          {
            "lang": "en",
            "value": "Code"
          }
        ],
        "persistence": {
          "type": "property",
          "column": "p_code"
        },
        "modifiers": {
          "unique": "true",
          "initial": "true",
          "optional": "false",
          "write": "false",
          "partof": "false"
        }
      },
      {
        "qualifier": "warehouse",
        "type": "Warehouse",
        "typeClass": "de.hybris.platform.ordersplitting.model.WarehouseModel",
        "name": [
          {
            "lang": "en",
            "value": "Warehouse"
          }
        ],
        "persistence": {
          "type": "property",
          "column": "p_warehouse"
        },
        "modifiers": {
          "unique": "false",
          "initial": "true",
          "optional": "false",
          "write": "true",
          "partof": "false"
        }
      },
      {
        "qualifier": "vehicle",
        "type": "Vehicle",
        "typeClass": "com.sap.cx.boosters.easy.deliveryslotmanagement.models.VehicleModel",
        "name": [
          {
            "lang": "en",
            "value": "Vehicle"
          }
        ],
        "persistence": {
          "type": "property",
          "column": "p_vehicle"
        },
        "modifiers": {
          "unique": "false",
          "initial": "true",
          "optional": "false",
          "write": "true",
          "partof": "false"
        }
      },
      {
        "qualifier": "starttime",
        "type": "java.util.Date",
        "name": [
          {
            "lang": "en",
            "value": "Start Time"
          }
        ],
        "persistence": {
          "type": "property",
          "column": "p_starttime"
        },
        "modifiers": {
          "unique": "false",
          "initial": "true",
          "optional": "false",
          "write": "true",
          "partof": "false"
        }
      },
      {
        "qualifier": "endtime",
        "type": "java.util.Date",
        "name": [
          {
            "lang": "en",
            "value": "End Time"
          }
        ],
        "persistence": {
          "type": "property",
          "column": "p_endtime"
        },
        "modifiers": {
          "unique": "false",
          "initial": "true",
          "optional": "false",
          "write": "true",
          "partof": "false"
        }
      },
      {
        "qualifier": "available",
        "type": "java.lang.Integer",
        "name": [
          {
            "lang": "en",
            "value": "Available"
          }
        ],
        "persistence": {
          "type": "property",
          "column": "p_available"
        },
        "modifiers": {
          "unique": "false",
          "initial": "true",
          "optional": "false",
          "write": "true",
          "partof": "false"
        }
      }
    ]
  },
  {
    "code": "DeliverySlotManagement",
    "name": [
      {
        "lang": "en",
        "value": "Delivery Slot Management"
      }
    ],
    "autocreate": "true",
    "generate": "true",
    "modelClassName": "DeliverySlotManagementModel",
    "superType": "GenericItem",
    "deployment": {
      "table": "deliveryslotmanagements",
      "typecode": "26002",
      "propstable": "deliveryslotmanagementprops"
    },
    "attributes": [
      {
        "qualifier": "code",
        "type": "java.lang.String",
        "name": [
          {
            "lang": "en",
            "value": "Code"
          }
        ],
        "persistence": {
          "type": "property",
          "column": "p_code"
        },
        "modifiers": {
          "unique": "true",
          "initial": "true",
          "optional": "false",
          "write": "false",
          "partof": "false"
        }
      },
      {
        "qualifier": "deliveryslot",
        "type": "DeliverySlot",
        "typeClass": "com.sap.cx.boosters.easy.deliveryslotmanagement.models.DeliverySlotModel",
        "name": [
          {
            "lang": "en",
            "value": "Delivery Slot"
          }
        ],
        "persistence": {
          "type": "property",
          "column": "p_deliveryslot"
        },
        "modifiers": {
          "unique": "false",
          "initial": "true",
          "optional": "false",
          "write": "true",
          "partof": "false"
        }
      },
      {
        "qualifier": "abstractorder",
        "type": "AbstractOrder",
        "typeClass": "de.hybris.platform.core.model.order.AbstractOrderModel",
        "name": [
          {
            "lang": "en",
            "value": "Abstract Order"
          }
        ],
        "persistence": {
          "type": "property",
          "column": "p_abstractorder"
        },
        "modifiers": {
          "unique": "false",
          "initial": "true",
          "optional": "false",
          "write": "true",
          "partof": "false"
        }
      },
      {
        "qualifier": "timestamp",
        "type": "java.util.Date",
        "name": [
          {
            "lang": "en",
            "value": "Timestamp"
          }
        ],
        "persistence": {
          "type": "property",
          "column": "p_timestamp"
        },
        "modifiers": {
          "unique": "false",
          "initial": "true",
          "optional": "false",
          "write": "true",
          "partof": "false"
        }
      },
      {
        "qualifier": "status",
        "type": "DeliverySlotStatus",
        "typeClass": "com.sap.cx.boosters.easy.deliveryslotmanagement.enums.DeliverySlotStatus",
        "name": [
          {
            "lang": "en",
            "value": "Status"
          }
        ],
        "persistence": {
          "type": "property",
          "column": "p_status"
        },
        "modifiers": {
          "unique": "false",
          "initial": "true",
          "optional": "false",
          "write": "true",
          "partof": "false"
        }
      }
    ]
  }
]
```

### Generate the model classes
This second step is required to use your own model class for the newly created itemtypes. In the traditional Commerce extensions, model classes are generated by the build process directly from the items.xml structure.
For Easy extensions, the model classes are generated through the gradle task `easy-class-gen` and then you need to register the model classes in the `easy.properties` file of the extension
Here are the properties that you need to set up:
```
easyextension.EasyTutorialStep2.easy.type.base.models.package=com.sap.cx.boosters.easy.deliveryslotmanagement
easytype.Vehicle.modelClass=com.sap.cx.boosters.easy.deliveryslotmanagement.models.VehicleModel
easytype.DeliverySlot.modelClass=com.sap.cx.boosters.easy.deliveryslotmanagement.models.DeliverySlotModel
easytype.DeliverySlotStatus.modelClass=com.sap.cx.boosters.easy.deliveryslotmanagement.enums.DeliverySlotStatus
```
## Importing data on Easy Extension
Let's see how you can import data into Easy Extension.
During the installation process, each easy extension can load data through impex files that are stored under the `impex/install` folder.
If you want to revert the loading of data during the uninstallation process, you can store impex files for removal under the `impex/uninstall` folder.

## Tutorial tasks
These are your tasks to complete this step of the tutorial:
- Complete the creation of the data model with the missing item types
- Create also the model classes for the new itemtypes
- Create ImpEx files to load delivery slots for the next 2 weeks. Create for every day of the week a slot for each of this time windows:
  - 10:00 to 12:00
  - 12:00 to 14:00
  - 14:00 to 16:00
  - 16:00 to 18:00
  - 18:00 to 20:00
  - 20:00 to 22:00
- Create ImpEx file to remove delivery slots created above during uninstall process

## Next step
Once completed, simply uninstall this Easy Tutorial Step 2 extension and install the next one: [Easy Tutorial Step 3](../easy-tutorial-step3/README.md) extension. In this next tutorial step you'll find also the solution to your task and you can compare it with yours.
