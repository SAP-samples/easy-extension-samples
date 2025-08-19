package com.sap.cx.boosters.easy.easytutorialstep6.controller

import com.sap.cx.boosters.easy.easytutorialstep6.service.DeliverySlotService
import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController
import de.hybris.platform.commerceservices.order.dao.CommerceOrderDao
import groovy.json.JsonBuilder

import jakarta.annotation.Resource

class ConfirmDeliveryController implements EasyRestServiceController {

    @Resource
    private DeliverySlotService deliverySlotService

    @Resource
    private CommerceOrderDao commerceOrderDao


    @Override
    Map<String, Object> execute(Map<String, Object> ctx) {
        def response = [:] as Map<String, Object>
        def orderId = ctx.pathParameters.orderId
        def deliverySlotManagementCode = ctx.pathParameters.deliverySlotManagementCode?.toString()

        def orderList = commerceOrderDao.find([code: orderId])
        def order = (orderList && !orderList.isEmpty()) ? orderList.get(0) : null
        def slotManagement = deliverySlotService.confirmDelivery(deliverySlotManagementCode, order)
        if (slotManagement) {
            response.'responseCode' = 200
            def slotManagementData = [
                    code        : slotManagement.getCode(),
                    deliverySlot: slotManagement.getDeliveryslot().getCode(),
                    order       : slotManagement.getAbstractorder().getCode(),
                    status      : slotManagement.getStatus().getCode(),
                    timestamp   : slotManagement.getTimestamp()
            ]
            response.'body' = new JsonBuilder(slotManagementData).toPrettyString()
        } else {
            response.'responseCode' = 500
            response.'body' = "Something wrong! We could not confirm the booking of the delivery Slot"
        }
        return response
    }
}
