package com.sap.cx.boosters.easy.easytutorialstep6.populators


import de.hybris.platform.commercefacades.order.data.OrderData
import de.hybris.platform.converters.Populator
import de.hybris.platform.core.model.order.OrderModel
import de.hybris.platform.servicelayer.dto.converter.ConversionException

import java.time.ZoneId
import java.time.format.DateTimeFormatter

class HomeDeliveryModeOrderPopulator implements Populator<OrderModel, OrderData> {

    @Override
    void populate(OrderModel source, OrderData orderData) throws ConversionException {
        if (source.deliveryMode.code.startsWith('homedelivery')) {
            def dsm = source.getProperty('deliveryslotmanagement')
            orderData.deliveryMode.description = "You'll receive your items on: ${dsm.getDeliveryslot().getStarttime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}"
        }
    }
}
