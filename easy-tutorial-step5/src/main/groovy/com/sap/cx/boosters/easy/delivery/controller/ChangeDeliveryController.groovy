package com.sap.cx.boosters.easy.delivery.controller

import com.sap.cx.boosters.easy.delivery.service.DeliverySlotService
import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController
import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao
import groovy.json.JsonBuilder

import javax.annotation.Resource

class ChangeDeliveryController extends BaseController implements EasyRestServiceController {

    @Resource
    DeliverySlotService deliverySlotService

    @Override
    Map<String, Object> execute(Map<String, Object> ctx) {

        def response = [:]
        def deliverySlotCode = ctx.parameters.deliverySlotCode

        // TODO With proper filter in place we should simply retrieve cart from the session
        def cart = getCart(ctx)

        def slotManagement = deliverySlotService.changeDelivery(deliverySlotCode,cart)
        if (slotManagement) {
            response.'responseCode' = 200
            def slotManagementData = [
                    code:slotManagement.getCode(),
                    deliverySlot: slotManagement.getDeliveryslot().getCode(),
                    cart: userId.equalsIgnoreCase("anonymous")?slotManagement.getAbstractorder().getGuid():slotManagement.getAbstractorder().getCode(),
                    status: slotManagement.getStatus().getCode(),
                    timestamp:slotManagement.getTimestamp()
            ]
            response.'body' = new JsonBuilder(slotManagementData).toPrettyString()
        } else {
            response.'responseCode' = 500
            response.'body' = "Something wrong! We could not change the delivery slot for the given booking"
        }

        return response

    }

}
