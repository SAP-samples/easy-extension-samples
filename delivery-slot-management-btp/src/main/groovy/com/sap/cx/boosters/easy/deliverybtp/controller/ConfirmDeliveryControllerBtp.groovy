package com.sap.cx.boosters.easy.deliverybtp.controller


import org.springframework.http.HttpMethod

class ConfirmDeliveryControllerBtp extends AbstractBtpController {

    private static final CONFIRM_SLOT_URL = "/api/deliveryslotmanagements/confirmDelivery?deliverySlotManagementCode={deliverySlotManagementCode}&orderCode={orderCode}"


    @Override
    Map<String, Object> execute(Map<String, Object> ctx) {
        def orderId = ctx.pathParameters.orderId
        def deliverySlotManagementCode = ctx.pathParameters.deliverySlotManagementCode

        return getBtpResponse(HttpMethod.PUT, CONFIRM_SLOT_URL, ["deliverySlotManagementCode": deliverySlotManagementCode, "orderCode": orderId])
    }
}
