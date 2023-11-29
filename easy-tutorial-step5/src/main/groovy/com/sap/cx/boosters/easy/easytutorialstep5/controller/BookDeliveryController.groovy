package com.sap.cx.boosters.easy.easytutorialstep5.controller

import com.sap.cx.boosters.easy.easytutorialstep5.service.DeliverySlotService
import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController
import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.servicelayer.internal.dao.GenericDao
import groovy.json.JsonBuilder

import javax.annotation.Resource

class BookDeliveryController implements EasyRestServiceController {

    @Resource
    private DeliverySlotService deliverySlotService

    @Resource
    private GenericDao<CartModel> defaultCartGenericDao

    @Override
    Map<String, Object> execute(Map<String, Object> ctx) {
        def response = [:] as Map<String, Object>
        def userId = ctx.pathParameters.userId
        def cartId = ctx.pathParameters.cartId
        def deliverySlotCode = ctx.parameters.deliverySlotCode?.toString()

        // TODO With proper filter in place we should simply retrieve cart from the session
        def cart
        if (userId.equalsIgnoreCase("anonymous")) {
            def cartList = defaultCartGenericDao.find([guid: cartId])
            cart = (cartList && !cartList.isEmpty()) ? cartList.get(0) : null
        } else {
            def cartList = defaultCartGenericDao.find([code: cartId])
            cart = (cartList && !cartList.isEmpty()) ? cartList.get(0) : null
        }

        def slotManagement = deliverySlotService.bookDelivery(deliverySlotCode, cart)
        if (slotManagement) {
            response.'responseCode' = 200
            def slotManagementData = [
                    code        : slotManagement.getCode(),
                    deliverySlot: slotManagement.getDeliveryslot().getCode(),
                    cart        : userId.equalsIgnoreCase("anonymous") ? slotManagement.getAbstractorder().getGuid() : slotManagement.getAbstractorder().getCode(),
                    status      : slotManagement.getStatus().getCode(),
                    timestamp   : slotManagement.getTimestamp()
            ]
            response.'body' = new JsonBuilder(slotManagementData).toPrettyString()
        } else {
            response.'responseCode' = 500
            response.'body' = "Something wrong! We could not book the deliverySlot"
        }
        return response
    }
}
