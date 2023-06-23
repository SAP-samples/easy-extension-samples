package com.sap.cx.boosters.easy.delivery.controller

import com.sap.cx.boosters.easy.delivery.service.DeliverySlotService
import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController
import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao

class CancelDeliveryController implements EasyRestServiceController {
    DeliverySlotService deliverySlotService
    DefaultGenericDao<CartModel> defaultCartGenericDao


    @Override
    Map<String, Object> execute(Map<String, Object> ctx) {
        def response = [:]
        def userId = ctx.pathParameters.userId
        def cartId = ctx.pathParameters.cartId

        // TODO With proper filter in place we should simply retrieve cart from the session
        def cart
        if (userId.equalsIgnoreCase("anonymous")){
            def cartList = defaultCartGenericDao.find([guid:cartId])
            cart = (cartList && !cartList.isEmpty())?cartList.get(0):null
        }else{
            def cartList = defaultCartGenericDao.find([code:cartId])
            cart = (cartList && !cartList.isEmpty())?cartList.get(0):null
        }


        def deleted = deliverySlotService.cancelDelivery(cart)
        if (deleted){
            response.'responseCode' = 200
            response.'body' = "OK"
        }else{
            response.'responseCode' = 404
            response.'body' = "We couldn't cancel the requested slot management"
        }
        return response
    }
}