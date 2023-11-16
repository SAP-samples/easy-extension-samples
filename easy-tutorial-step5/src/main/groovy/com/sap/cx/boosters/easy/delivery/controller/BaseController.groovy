package com.sap.cx.boosters.easy.delivery.controller

import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao

import javax.annotation.Resource

class BaseController {

    @Resource
    DefaultGenericDao<CartModel> defaultCartGenericDao

    CartModel getCart(Map<String, Object> ctx) {

        def userId = ctx.pathParameters.userId
        def cartId = ctx.pathParameters.cartId

        // TODO With proper filter in place we should simply retrieve cart from the session
        CartModel cart

        if (userId.equalsIgnoreCase('anonymous')) {
            def cartList = defaultCartGenericDao.find([guid: cartId])
            cart = (cartList && !cartList.isEmpty()) ? cartList.get(0) : null
        } else {
            def cartList = defaultCartGenericDao.find([code: cartId])
            cart = (cartList && !cartList.isEmpty()) ? cartList.get(0) : null
        }

    }

}
