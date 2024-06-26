package com.sap.cx.boosters.easy.easytutorialstep5.controller

import com.sap.cx.boosters.easy.easytutorialstep5.service.DeliverySlotService
import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController
import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.ordersplitting.WarehouseService
import de.hybris.platform.servicelayer.config.ConfigurationService
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao
import groovy.json.JsonBuilder

import javax.annotation.Resource
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class AvailableSlotsController implements EasyRestServiceController {

    @Resource
    private WarehouseService warehouseService

    @Resource
    private DeliverySlotService deliverySlotService

    @Resource
    private DefaultGenericDao<CartModel> defaultCartGenericDao

    @Resource
    private ConfigurationService configurationService

    @Override
    Map<String, Object> execute(Map<String, Object> ctx) {
        def response = [:] as Map<String, Object>
        LocalDate now = LocalDate.now()
        LocalDateTime startTime = now.plusDays(configurationService.getConfiguration().getInt("homeDelivery.minimumLeadTime", 1)).atStartOfDay()
        LocalDateTime endTime = now.plusDays(configurationService.getConfiguration().getInt("homeDelivery.maximumLeadTime", 7)).atStartOfDay()
        def userId = ctx.pathParameters.userId?.toString()
        def cartId = ctx.pathParameters.cartId?.toString()

        // TODO With proper filter in place we should simply retrieve cart from the session
        CartModel cart
        if (userId.equalsIgnoreCase("anonymous")) {
            def cartList = defaultCartGenericDao.find([guid: cartId])
            cart = (cartList && !cartList.isEmpty()) ? cartList.get(0) : null
        } else {
            def cartList = defaultCartGenericDao.find([code: cartId])
            cart = (cartList && !cartList.isEmpty()) ? cartList.get(0) : null
        }
        // Here we should have the logic to retrieve the warehouse for the user. As of now we simply
        // retrieve the default delivery origin warehouse associated to the base store related to the cart
        def warehouseCode = cart.getStore().getDefaultDeliveryOrigin().getName()
        def warehouse = warehouseService.getWarehouseForCode(warehouseCode)

        def availableSlots = deliverySlotService.getAvailableDeliverySlots(Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant()), Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant()), warehouse)

        def availableSlotsMap = availableSlots.isEmpty() ? [:] : availableSlots.collectEntries { [it.code, it.code] }
        def allSlots = deliverySlotService.getAllDeliverySlots(Date.from(startTime.atZone(ZoneId.systemDefault()).toInstant()), Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant()), warehouse)
        if (availableSlots) {
            response.'responseCode' = 200
            def allSlotsData = buildSlotsData(allSlots, availableSlotsMap)
            response.'body' = new JsonBuilder(allSlotsData).toPrettyString()
        } else {
            response.'responseCode' = 404
            def allSlotsData = buildSlotsData(allSlots, availableSlotsMap)
            response.'body' = new JsonBuilder(allSlotsData).toPrettyString()
        }
        return response
    }

    static def buildSlotsData(allSlots, availableSlotsMap) {
        def slotsGroupedByDays = allSlots.groupBy { it.getStarttime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfWeek() }
        def allSlotsData = [
                totalDays         : slotsGroupedByDays.size(),
                slotsPerDay       : slotsGroupedByDays.size() > 0 ? slotsGroupedByDays.values().stream().findFirst().get().size() : 0,
                slotsConfiguration: allSlots.size() > 0 ? allSlots.collect {
                    [
                            code     : it.getCode(),
                            vehicle  : it.getVehicle().getCode(),
                            warehouse: it.getWarehouse().getCode(),
                            startTime: it.getStarttime(),
                            endTime  : it.getEndtime(),
                            weekDay  : it.getStarttime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().getDayOfWeek(),
                            available: availableSlotsMap.containsKey(it.getCode())
                    ]
                } : null
        ]
        return allSlotsData
    }

}
