package com.sap.cx.boosters.easy.easytutorialstep4.service

import com.sap.cx.boosters.easy.easytutorialstep4.models.DeliverySlotManagementModel
import com.sap.cx.boosters.easy.easytutorialstep4.models.DeliverySlotModel
import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.core.model.order.OrderModel
import de.hybris.platform.ordersplitting.model.WarehouseModel

interface DeliverySlotService {

    List<DeliverySlotModel> getAvailableDeliverySlots(Date start, Date end, WarehouseModel warehouse)

    List<DeliverySlotModel> getAllDeliverySlots(Date start, Date end, WarehouseModel warehouse)

    DeliverySlotManagementModel confirmDelivery(String deliverySlotManagementCode, OrderModel order)

    DeliverySlotManagementModel changeDelivery(String deliverySlotCode, CartModel cart)

    DeliverySlotManagementModel bookDelivery(String deliverySlotCode, CartModel cart)

    boolean cancelDelivery(CartModel cart)

    DeliverySlotManagementModel getBookedSlot(CartModel cart)

    DeliverySlotModel findSlotByCode(String code)

    DeliverySlotManagementModel findSlotManagementByCode(String code)

}
