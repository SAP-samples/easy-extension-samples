package com.sap.cx.boosters.easy.easytutorialstep6.populators

import com.sap.cx.boosters.easy.easytutorialstep6.models.DeliverySlotManagementOrderModel
import de.hybris.platform.commercefacades.order.data.OrderData
import de.hybris.platform.converters.Populator
import de.hybris.platform.core.model.order.OrderModel
import de.hybris.platform.servicelayer.dto.converter.ConversionException

import java.time.ZoneId
import java.time.format.DateTimeFormatter

class HomeDeliveryModeOrderPopulator implements Populator<OrderModel, OrderData> {

    @Override
    void populate(OrderModel source, OrderData orderData) throws ConversionException {
        if (source instanceof DeliverySlotManagementOrderModel) {
            def orderModel = source as DeliverySlotManagementOrderModel
            if (orderModel.getDeliveryMode().getCode().startsWith("homedelivery")) {
                def dsm = orderModel.getDeliveryslotmanagement()
                orderData.getDeliveryMode().setDescription("You'll receive your items on: ${dsm.getDeliveryslot().getStarttime().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))}")
            }
        }
    }
}
