# Easy Tutorial Step 3 - How to Create Spring Beans

## Overview
In this step of the tutorial, you will learn how to create your service classes to perform the back end operations for your home delivery feature.
You'll also learn how to register your service in the Spring context so that it can be used later on by your REST end points.

## Create the service
You can create the Groovy class of your backend services under the `src` folder of the easy extension.
We've implemented a few methods of the service as an example. As you can see the service can make use also of other beans registered in the Spring context.

## Register the service
Service has been registered in the Spring context using the `EasyBeans.groovy` script.

```groovy
easyCoreBeans {

	deliverySlotService(com.sap.cx.boosters.easy.delivery.service.DeliverySlotService)

}
```

Note in this example dependencies are inject using `@javax.annotation.Resource` annotations in `com.sap.cx.boosters.easy.delivery.service.DeliverySlotService`

Alternative syntax is available:

```groovy
easyCoreBeans {

	deliverySlotService(com.sap.cx.boosters.easy.delivery.service.DeliverySlotService) {
        flexibleSearchService = ref('flexibleSearchService')
        modelService = ref('modelService')
        enumerationService = ref('enumerationService')
    }

}
```

or:

```groovy
easyCoreBeans {

	deliverySlotService(com.sap.cx.boosters.easy.delivery.service.DeliverySlotService) {beanDef ->
        beanDef.flexibleSearchService = flexibleSearchService
        beanDef.modelService = modelService
        beanDef.enumerationService = enumerationService
    }

}
```

## Your mission

These are your tasks to complete this tutorial step:
- Complete the implementation of the Service methods that are tagged as TODO
- Create unit tests for each implemented method
If you want to practice also registering beans in the spring context, you can create DAOs that the service can use for the read access to the database

## Next step
Once completed, simply uninstall this Easy Tutorial Step 3 extension and install the next one: [Easy Tutorial Step 4](../easy-tutorial-step4/README.md) extension. In this next tutorial step you'll find also the solution to your task and you can compare it with yours.
