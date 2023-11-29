package com.sap.cx.boosters.easy.deliverybtp.controller


import org.springframework.http.HttpMethod

class ChangeDeliveryControllerBtp extends AbstractBtpController {

    private static final CHANGE_SLOT_URL = "/api/deliveryslotmanagements/changeDelivery?deliverySlotCode={deliverySlotCode}&cartCode={cartCode}"

    @Override
    Map<String, Object> execute(Map<String, Object> ctx) {
        def cartId = ctx.pathParameters.cartId
        def deliverySlotCode = ctx.parameters.deliverySlotCode

        return getBtpResponse(HttpMethod.PUT, CHANGE_SLOT_URL, ["deliverySlotCode": deliverySlotCode, "cartCode": cartId])
    }
}
