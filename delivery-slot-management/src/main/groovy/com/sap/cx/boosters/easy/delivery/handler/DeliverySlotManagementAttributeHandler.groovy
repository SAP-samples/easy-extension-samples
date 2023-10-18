package com.sap.cx.boosters.easy.delivery.handler

import de.hybris.platform.core.model.order.AbstractOrderModel
import de.hybris.platform.servicelayer.model.attribute.AbstractDynamicAttributeHandler
import de.hybris.platform.servicelayer.search.FlexibleSearchService

import javax.annotation.Resource

class DeliverySlotManagementAttributeHandler extends AbstractDynamicAttributeHandler<Object, AbstractOrderModel>{

    @Resource
    FlexibleSearchService flexibleSearchService

    public static final QUERY_GET_ABSTRACT_ORDER = 'select {pk} from {DeliverySlotManagement as dsm} where {dsm.abstractorder} = ?abstractorder'

    @Override
    def get(AbstractOrderModel abstractorderModel) {
        def result = flexibleSearchService.search(QUERY_GET_ABSTRACT_ORDER,[abstractorder:abstractorderModel])
        result.getCount()>0?result.getResult().get(0):null
    }

}
