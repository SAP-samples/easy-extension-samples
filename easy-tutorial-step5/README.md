# Easy Tutorial Step 5

## Overview
This step of the tutorial will show you how to create the backoffice configuration on an Easy extension.


## Create the configuration
For simple backoffice configuration you can create a `-backoffice-config.xml` file containing the configuration and put it under the `backoffice` folder at the root of yout Easy extension.
Similarly, localization labels used by the backoffice configuration can be created under the `backoffice/labels` folder inside `labels.properties` or `labels_<language>.properties` files

## Tutorial tasks
These are your tasks to complete this tutorial step:
- create backoffice configuration for DeliverySlot itemtype: possibility to search for it and browse each DeliverySlot item in the backoffice Admin cockpit. You can use same Tree node used for Vehicle
- create backoffice configuration for DeliverySlotManagement itemtype: possibility to search the booking and browse the details in the backoffice Admin cockpit. You can use same Tree node used for Vehicle
- create backoffice configuration to see the related booking associated to a cart in the backoffice Admin cockpit
- create backoffice configuration to see the related booking associated to an order both in the backoffice Admin cockpit and in the Customer Support cockpit
Tip for the last 2 tasks: given the data model of the Easy extension, you can create a dynamic attribute for both Cart and Order itemtypes that allow you to browse the one-to-one relation between Abstract Order and Delivery Slot Management. Then you can use the dynamic attribute in the backoffice config.

## Next Step
Once completed, simply uninstall this Easy Tutorial Step 5 extension and install the next one: Easy Tutorial Step 6 extension. In this next tutorial step you'll find also the solution to your task and you can compare it with yours.