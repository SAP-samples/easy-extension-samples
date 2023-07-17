package com.sap.cx.boosters.easy.deliverybtp.controller


import org.springframework.http.HttpMethod

class GetBookedDeliveryControllerBtp extends AbstractBtpController {

    private static final GET_BOOKED_SLOT_URL = "/api/deliveryslotmanagements/getBookedSlot?cartCode={cartCode}"

    @Override
    Map<String, Object> execute(Map<String, Object> ctx) {
        def response = [:]
        def userId = ctx.pathParameters.userId
        def cartId = ctx.pathParameters.cartId

        def cart = getCart(userId,cartId)

        return getBtpResponse(HttpMethod.GET,GET_BOOKED_SLOT_URL,["cartCode":cartId])
    }
}
