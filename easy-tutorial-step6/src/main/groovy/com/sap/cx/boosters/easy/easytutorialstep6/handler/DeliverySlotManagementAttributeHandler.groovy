package com.sap.cx.boosters.easy.easytutorialstep6.handler

import com.sap.cx.boosters.easy.easytutorialstep6.models.DeliverySlotManagementModel
import de.hybris.platform.core.model.order.AbstractOrderModel
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler
import de.hybris.platform.servicelayer.search.FlexibleSearchService

import jakarta.annotation.Resource

class DeliverySlotManagementAttributeHandler extends AbstractDynamicAttributeHandler<DeliverySlotManagementModel, AbstractOrderModel> {
    private static final def QUERY_GET_ABSTRACT_ORDER = 'select {pk} from {DeliverySlotManagement as dsm} where {dsm.abstractorder} = ?abstractorder'

    @Resource
    private FlexibleSearchService flexibleSearchService

    @Override
    DeliverySlotManagementModel get(AbstractOrderModel abstractOrderModel) {
        def result = flexibleSearchService.<DeliverySlotManagementModel> search(QUERY_GET_ABSTRACT_ORDER, [abstractorder: abstractOrderModel])
        return result.getCount() > 0 ? result.getResult().get(0) : null
    }
}
