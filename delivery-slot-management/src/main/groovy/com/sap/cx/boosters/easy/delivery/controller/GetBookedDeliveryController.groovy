package com.sap.cx.boosters.easy.delivery.controller

import com.sap.cx.boosters.easy.delivery.service.DeliverySlotService
import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController
import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.ordersplitting.WarehouseService
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao
import groovy.json.JsonBuilder

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class GetBookedDeliveryController implements EasyRestServiceController {
    DeliverySlotService deliverySlotService
    DefaultGenericDao<CartModel> defaultCartGenericDao

    @Override
    Map<String, Object> execute(Map<String, Object> ctx) {
        def response = [:]
        def userId = ctx.pathParameters.userId
        def cartId = ctx.pathParameters.cartId

        // TODO With proper filter in place we should simply retrieve cart from the session
        def CartModel cart
        if (userId.equalsIgnoreCase("anonymous")){
            def cartList = defaultCartGenericDao.find([guid:cartId])
            cart = (cartList && !cartList.isEmpty())?cartList.get(0):null
        }else{
            def cartList = defaultCartGenericDao.find([code:cartId])
            cart = (cartList && !cartList.isEmpty())?cartList.get(0):null
        }

        def slotManagement = deliverySlotService.getBookedSlot(cart)
        if (slotManagement){
            response.'responseCode' = 200
            def slotManagementData = [
                    code:slotManagement.getCode(),
                    deliverySlot: slotManagement.getDeliveryslot().getCode(),
                    cart: userId.equalsIgnoreCase("anonymous")?slotManagement.getAbstractorder().getGuid():slotManagement.getAbstractorder().getCode(),
                    status: slotManagement.getStatus().getCode(),
                    timestamp:slotManagement.getTimestamp()
            ]
            response.'body' = new JsonBuilder(slotManagementData).toPrettyString()
        }else{
            response.'responseCode' = 404
            response.'body' = "No booked slot for the given cart"
        }
        return response
    }
}
