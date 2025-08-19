package com.sap.cx.boosters.easy.deliveryslotmanagement.service.impl

import com.sap.cx.boosters.easy.deliveryslotmanagement.service.DeliverySlotCleanupService
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import org.slf4j.LoggerFactory

import jakarta.annotation.Resource

class DefaultDeliverySlotCleanupService implements DeliverySlotCleanupService {

    private static final def QUERY_GET_ABSTRACT_ORDERS_TO_DELETE = "select {pk} from {AbstractOrder as ao join DeliveryMode as dm on {dm.pk}={ao.deliveryMode}} where {dm.code} in  ('homedelivery-net','homedelivery-gross')"

    private static final def LOG = LoggerFactory.getLogger(DefaultDeliverySlotCleanupService)

    @Resource
    private FlexibleSearchService flexibleSearchService

    @Resource
    private ModelService modelService

    @Override
    Integer deleteHomeDeliveryAbstractOrders(){
        def result = flexibleSearchService.search(QUERY_GET_ABSTRACT_ORDERS_TO_DELETE)
        def abstractOrders = result.getResult()
        modelService.removeAll(abstractOrders)
        LOG.info("Total abstractOrders removed:"  + abstractOrders.size())
        abstractOrders.size()
    }

}
