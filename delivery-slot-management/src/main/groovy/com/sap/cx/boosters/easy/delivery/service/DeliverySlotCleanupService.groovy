package com.sap.cx.boosters.easy.delivery.service

import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.servicelayer.search.FlexibleSearchService

import javax.annotation.Resource

class DeliverySlotCleanupService {

    public static final QUERY_GET_ABSTRACT_ORDERS_TO_DELETE = 'select {pk} from {AbstractOrder as ao join DeliveryMode as dm on {dm.pk} = {ao.deliveryMode}} where {dm.code} in  (\'homedelivery-net\',\'homedelivery-gross\')'

    def LOG = org.slf4j.LoggerFactory.getLogger('DeliverySlotCleanupService')

    @Resource
    FlexibleSearchService flexibleSearchService

    @Resource
    ModelService modelService

    Integer deleteHomeDeliveryAbstractOrders() {
        def searchResult = flexibleSearchService.search(QUERY_GET_ABSTRACT_ORDERS_TO_DELETE)
        def abstractOrders = searchResult.result
        modelService.removeAll(abstractOrders)
        LOG.info "Total abstractOrders removed: ${abstractOrders.size()}"
        abstractOrders.size()
    }

}
