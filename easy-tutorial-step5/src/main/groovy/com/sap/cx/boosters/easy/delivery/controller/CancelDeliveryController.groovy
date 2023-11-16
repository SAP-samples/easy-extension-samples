package com.sap.cx.boosters.easy.delivery.controller

import com.sap.cx.boosters.easy.delivery.service.DeliverySlotService
import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController
import com.sun.tools.rngom.parse.host.Base
import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao
import de.hybris.platform.servicelayer.user.UserService

import javax.annotation.Resource

class CancelDeliveryController extends BaseController implements EasyRestServiceController {

    @Resource
    DeliverySlotService deliverySlotService

    @Override
    Map<String, Object> execute(Map<String, Object> ctx) {

        def response = [:]

        def cart = getCart(ctx)

        def deleted = deliverySlotService.cancelDelivery(cart)

        if (deleted){
            response.'responseCode' = 200
            response.'body' = "OK"
        } else {
            response.'responseCode' = 404
            response.'body' = "We couldn't cancel the requested slot management"
        }

        return response

    }

}
