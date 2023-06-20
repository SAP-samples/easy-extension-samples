package com.sap.cx.boosters.easy.delivery.populators


import de.hybris.platform.commercefacades.order.data.OrderData
import de.hybris.platform.converters.Populator
import de.hybris.platform.core.model.order.OrderModel
import de.hybris.platform.servicelayer.dto.converter.ConversionException

import java.time.ZoneId
import java.time.format.DateTimeFormatter

public class HomeDeliveryModeOrderPopulator implements Populator<OrderModel, OrderData> {



    @Override
    public void populate(OrderModel orderModel, OrderData orderData) throws ConversionException {
        if (orderModel.getDeliveryMode().getCode().startsWith("homedelivery")){
            def dsm = orderModel.getDeliveryslotmanagement()
            orderData.getDeliveryMode().setDescription("You'll receive your items on: " + dsm.getDeliveryslot().getStarttime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")))
        }
    }
}