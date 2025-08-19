package com.sap.cx.boosters.easy.easytutorialstep3.service.impl


import com.sap.cx.boosters.easy.easytutorialstep3.models.DeliverySlotManagementModel
import com.sap.cx.boosters.easy.easytutorialstep3.models.DeliverySlotModel
import com.sap.cx.boosters.easy.easytutorialstep3.service.DeliverySlotService
import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.core.model.order.OrderModel
import de.hybris.platform.ordersplitting.model.WarehouseModel
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.servicelayer.search.FlexibleSearchService

import jakarta.annotation.Resource

class DefaultDeliverySlotService implements DeliverySlotService {


    @Resource
    private FlexibleSearchService flexibleSearchService

    @Resource
    private ModelService modelService

    @Override
    List<DeliverySlotModel> getAvailableDeliverySlots(Date start, Date end, WarehouseModel warehouse) {
        // TODO Should return the list of available slots for the given Warehouse and start and end date. To find available slots we need to subtract
        // from "DeliverySlot.available" matching row cont in "DeliverySlotManagement".
        // Only DeliverySlots with:
        //select {pk} from {DeliverySlot as ds} where {ds.warehouse} = ?warehouse and {ds.starttime} > ?start and {ds.endtime} < ?end and ({ds.available} - ({{select count(*) from {DeliverySlotManagement as dsm} where {dsm.deliveryslot}={ds.pk}}}))>0
        //has to be returned
        return null
    }

    @Override
    List<DeliverySlotModel> getAllDeliverySlots(Date start, Date end, WarehouseModel warehouse) {
        //TODO Should return the list of all slots for the given Warehouse and start and end date
        return null
    }

    @Override
    DeliverySlotManagementModel confirmDelivery(String deliverySlotManagementCode, OrderModel order) {
        // TODO The operation retrieve the DeliverySlotManagementModel given the passed code parameter, check if the item is in the BOOKED status and then perform the update,
        // setting the order in place of the cart and updating the status to CONFIRMED.
        return null
    }

    @Override
    DeliverySlotManagementModel changeDelivery(String deliverySlotCode, CartModel cart) {
        // TODO This operation is used when a customer change the delivery slot previously booked.
        //The service retrieves the deliverySlotManagement associated to the passed cart and if it exists and is still in the BOOKED status and if the passed delivery slot
        // is still available, then it performs the update
        return null
    }

    @Override
    DeliverySlotManagementModel bookDelivery(String deliverySlotCode, CartModel cart) {
        //TODO This operation should create the instance of DeliverySlotManagementModel for the cart to carry the information of the delivery slot selected for cart
        // If a slot is already booked then you should skip creating another one
        return null
    }

    @Override
    DeliverySlotManagementModel getBookedSlot(CartModel cart) {
        //TODO Use flexible search service to retrieve the instance of DeliverySlotManagementModel for the cart.
        // Return null of there is no booked delivery found for cart. You can use a query like:
        // SELECT {pk} FROM {DeliverySlotManagement AS dsm JOIN Cart AS c ON {dsm.abstractorder}={c.pk}} WHERE {c.pk} = ?cart
        return null
    }

    @Override
    DeliverySlotModel findSlotByCode(String code) {
        //TODO Use flexible search service to retrieve an available DeliverySlot for a the Delivery Slot Code.
        // The availability of a delivery slot can be evaluated by subtracting the number of Booked Delivery Slots (Delivery Slot Management) from the available value of Delivery Slot
        // Return null of there is no delivery slot found for the given code. You can use a query like:
        // SELECT {pk} FROM {DeliverySlot as ds} WHERE {ds.code} = ?slotCode AND ({ds.available} - ({{SELECT count(*) FROM {DeliverySlotManagement AS dsm} WHERE {dsm.deliveryslot}={ds.pk}}}))>0
        return null
    }

}
