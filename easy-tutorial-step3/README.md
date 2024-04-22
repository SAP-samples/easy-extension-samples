# Easy Tutorial Step 3 - How to Create Spring Beans

## Overview
In this step of the tutorial, you will learn how to create your implementation classes to perform the back end operations for your home delivery feature.
You'll also learn how to register your implementation classes in the Spring context so that it can be used later on by your REST end points.

## Create the service
You can create the Groovy class of your backend services under the `src` folder of the easy extension.
We've implemented a few methods of the service as an example. As you can see the service can make use also of other beans registered in the Spring context.

## Create the Delivery Slot Generator
You can create a Groovy class to generate the delivery slots dynamically for next `X` days or for a duration. After registering it as a spring bean you can use the spring bean to generate the delivery slots dynamically as part of ImpEx import. 

## Beanshell in ImpEx script for generating the delivery slots
Once you have defined the delivery slot generator, you can call the respective generator method in the ImpEx import script as:

```beanshell
"#%
de.hybris.platform.core.Registry.getApplicationContext().getBean(""deliverySlotGenerator"").generateSlotsForNextXDays(""warehouse_e"", 30, ""Vehicle1"", 3);
de.hybris.platform.core.Registry.getApplicationContext().getBean(""deliverySlotGenerator"").generateSlotsForNextXDays(""ap_warehouse_e"", 30, ""Vehicle1"", 3);
"
```

## Register the service
Services can be registered in the Spring context using the `EasyBeans.groovy` script as:
```groovy
easyCoreBeans {
    vehicleGenericDao(DefaultGenericDao, VehicleModel._TYPECODE)

    vehicleService(DefaultVehicleService)

    deliverySlotGenerator(DefaultDeliverySlotGenerator)
    
    deliverySlotService(DefaultDeliverySlotService)
}
```

We can use annotation based dependency injection in the implementation classes as:
```
    @Required
    private ModelService modelService
```

With this the dependencies are automatically injected by just defining the service in `EasyBeans.groovy` script.

## Writing the Unit tests
You can create your test classes in `src/test/groovy` similar to the SAP Commerce unit tests. You can use the `Mockito` framework and the `@UnitTest` annotation to distinguish the tests.

## Your mission
These are your tasks to complete this tutorial step:
- Complete the implementation of the Service methods that are tagged as TODO
- Create unit tests for each implemented method

## Next step
Once completed, simply uninstall this Easy Tutorial Step 3 extension and install the next one: [Easy Tutorial Step 4](../easy-tutorial-step4/README.md) extension. In this next tutorial step you'll find also the solution to your task, and you can compare it with yours.
