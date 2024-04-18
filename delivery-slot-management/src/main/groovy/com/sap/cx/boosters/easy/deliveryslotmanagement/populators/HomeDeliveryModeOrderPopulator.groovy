package com.sap.cx.boosters.easy.deliveryslotmanagement.populators

import com.sap.cx.boosters.easy.deliveryslotmanagement.models.DeliverySlotManagementModel
import de.hybris.platform.commercefacades.order.data.OrderData
import de.hybris.platform.converters.Populator
import de.hybris.platform.core.model.order.OrderModel
import de.hybris.platform.servicelayer.dto.converter.ConversionException

import java.time.ZoneId
import java.time.format.DateTimeFormatter

class HomeDeliveryModeOrderPopulator implements Populator<OrderModel, OrderData> {

    @Override
    void populate(OrderModel source, OrderData orderData) throws ConversionException {
        if (source.getDeliveryMode().getCode().startsWith("homedelivery")) {
            DeliverySlotManagementModel dsm = source.getProperty('deliveryslotmanagement') as DeliverySlotManagementModel
            if (null != dsm) {
                orderData.deliveryMode.description = "You'll receive your items on: ${dsm.deliveryslot.starttime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}"
            }
        }
    }
}
