package com.sap.cx.boosters.easy.delivery.controller

import com.sap.cx.boosters.easy.delivery.service.DeliverySlotService
import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController
import de.hybris.platform.commerceservices.order.dao.impl.DefaultCommerceOrderDao
import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao
import groovy.json.JsonBuilder

class ConfirmDeliveryController implements EasyRestServiceController {
    DeliverySlotService deliverySlotService
    DefaultCommerceOrderDao commerceOrderDao


    @Override
    Map<String, Object> execute(Map<String, Object> ctx) {
        def response = [:]
        def orderId = ctx.pathParameters.orderId
        def deliverySlotManagementCode = ctx.pathParameters.deliverySlotManagementCode

        def orderList = commerceOrderDao.find([code:orderId])
        def order = (orderList && !orderList.isEmpty())?orderList.get(0):null
        def slotManagement = deliverySlotService.confirmDelivery(deliverySlotManagementCode,order)
        if (slotManagement){
            response.'responseCode' = 200
            def slotManagementData = [
                    code:slotManagement.getCode(),
                    deliverySlot: slotManagement.getDeliveryslot().getCode(),
                    order: slotManagement.getAbstractorder().getCode(),
                    status: slotManagement.getStatus().getCode(),
                    timestamp:slotManagement.getTimestamp()
            ]
            response.'body' = new JsonBuilder(slotManagementData).toPrettyString()
        }else{
            response.'responseCode' = 500
            response.'body' = "Something wrong! We could not confirm the booking of the delivery Slot"
        }
        return response
    }
}
