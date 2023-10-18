package com.sap.cx.boosters.easy.delivery.controller

import com.sap.cx.boosters.easy.delivery.service.DeliverySlotService
import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController
import de.hybris.platform.commerceservices.order.dao.impl.DefaultCommerceOrderDao
import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao
import groovy.json.JsonBuilder

import javax.annotation.Resource

class ChangeDeliveryController implements EasyRestServiceController {

    @Resource
    DeliverySlotService deliverySlotService

    @Resource
    DefaultGenericDao<CartModel> defaultCartGenericDao

    @Override
    Map<String, Object> execute(Map<String, Object> ctx) {

        def response = [:]
        def userId = ctx.pathParameters.userId as String
        def cartId = ctx.pathParameters.cartId as String
        def deliverySlotCode = ctx.parameters.deliverySlotCode as String

        // TODO With proper filter in place we should simply retrieve cart from the session
        def cart
        if (userId.equalsIgnoreCase("anonymous")){
            def cartList = defaultCartGenericDao.find([guid:cartId])
            cart = (cartList && !cartList.isEmpty())?cartList.get(0):null
        } else {
            def cartList = defaultCartGenericDao.find([code:cartId])
            cart = (cartList && !cartList.isEmpty())?cartList.get(0):null
        }

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
