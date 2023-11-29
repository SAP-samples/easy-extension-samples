package com.sap.cx.boosters.easy.deliverybtp.controller


import org.springframework.http.HttpMethod

class CancelDeliveryControllerBtp extends AbstractBtpController {

    private static final CANCEL_SLOT_URL = "/api/deliveryslotmanagements/cancelDelivery?cartCode={cartCode}"


    @Override
    Map<String, Object> execute(Map<String, Object> ctx) {
        def cartId = ctx.pathParameters.cartId

        return getBtpResponse(HttpMethod.DELETE, CANCEL_SLOT_URL, ["cartCode": cartId])
    }
}
