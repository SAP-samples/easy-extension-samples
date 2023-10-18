package com.sap.cx.boosters.easy.delivery.populators

import de.hybris.platform.commercefacades.order.data.OrderData
import de.hybris.platform.converters.Populator
import de.hybris.platform.core.model.order.OrderModel
import de.hybris.platform.servicelayer.dto.converter.ConversionException

import java.time.ZoneId
import java.time.format.DateTimeFormatter

class HomeDeliveryModeOrderPopulator implements Populator<OrderModel, OrderData> {

    @Override
    void populate(OrderModel orderModel, OrderData orderData) throws ConversionException {
        if (orderModel.deliveryMode.code.startsWith('homedelivery')){
            def dsm = orderModel.deliveryslotmanagement
            orderData.deliveryMode.description = "You'll receive your items on: ${dsm.deliveryslot.starttime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}"
        }
    }

}