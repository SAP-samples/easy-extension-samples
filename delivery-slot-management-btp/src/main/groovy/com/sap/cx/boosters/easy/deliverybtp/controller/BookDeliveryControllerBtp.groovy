package com.sap.cx.boosters.easy.deliverybtp.controller

import org.springframework.http.HttpMethod

class BookDeliveryControllerBtp extends AbstractBtpController {

    private static final BOOK_SLOT_URL = "/api/deliveryslotmanagements/bookDelivery?deliverySlotCode={deliverySlotCode}&cartCode={cartCode}"


    @Override
    Map<String, Object> execute(Map<String, Object> ctx) {
        def response = [:]
        def userId = ctx.pathParameters.userId
        def cartId = ctx.pathParameters.cartId
        def deliverySlotCode = ctx.parameters.deliverySlotCode

        def cart = getCart(userId,cartId)

        return getBtpResponse(HttpMethod.POST, BOOK_SLOT_URL,["cartCode":cartId, "deliverySlotCode":deliverySlotCode])
    }
}
