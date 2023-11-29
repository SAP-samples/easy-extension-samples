package com.sap.cx.boosters.easy.deliverybtp.controller

import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.ordersplitting.WarehouseService
import de.hybris.platform.servicelayer.config.ConfigurationService
import org.springframework.http.HttpMethod

import javax.annotation.Resource
import java.time.LocalDate
import java.time.LocalDateTime

class AvailableSlotsControllerBtp extends AbstractBtpController {

    private static final AVAILABLE_SLOTS_URL = "/api/deliveryslots/getAvailableSlots?warehousecode={warehousecode}&startTime={startTime}&endTime={endTime}"

    @Resource
    private WarehouseService warehouseService

    @Resource
    private ConfigurationService configurationService

    @Override
    Map<String, Object> execute(Map<String, Object> ctx) {
        LocalDate now = LocalDate.now()
        LocalDateTime startTime = now.plusDays(configurationService.getConfiguration().getInt("homeDelivery.minimumLeadTime", 1)).atStartOfDay()
        LocalDateTime endTime = now.plusDays(configurationService.getConfiguration().getInt("homeDelivery.maximumLeadTime", 7)).atStartOfDay()
        def userId = ctx.pathParameters.userId?.toString()
        def cartId = ctx.pathParameters.cartId?.toString()

        // TODO With proper filter in place we should simply retrieve cart from the session
        CartModel cart = getCart(userId, cartId)

        // Here we should have the logic to retrieve the warehouse for the user. As of now we simply
        // retrieve the default delivery origin warehouse associated to the base store related to the cart
        def warehouseCode = cart.getStore().getDefaultDeliveryOrigin().getName()
        def warehouse = warehouseService.getWarehouseForCode(warehouseCode)

        return getBtpResponse(HttpMethod.GET, AVAILABLE_SLOTS_URL, ["warehousecode": warehouse.getCode(),
                                                                    "startTime"    : startTime.toLocalDate().toString(),
                                                                    "endTime"      : endTime.toLocalDate().toString()])
    }

}
