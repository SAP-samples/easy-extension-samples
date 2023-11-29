package com.sap.cx.boosters.easy.deliveryslotmanagement.service.impl


import com.sap.cx.boosters.easy.deliveryslotmanagement.service.DeliverySlotService
import com.sap.cx.boosters.easy.deliveryslotmanagement.enums.DeliverySlotStatus
import com.sap.cx.boosters.easy.deliveryslotmanagement.models.DeliverySlotManagementModel
import com.sap.cx.boosters.easy.deliveryslotmanagement.models.DeliverySlotModel
import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.core.model.order.OrderModel
import de.hybris.platform.ordersplitting.model.WarehouseModel
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import org.slf4j.LoggerFactory

import javax.annotation.Resource

class DefaultDeliverySlotService implements DeliverySlotService {

    private static final def LOG = LoggerFactory.getLogger(DefaultDeliverySlotService)

    public static final QUERY_AVAILABLE_SLOTS = 'select {pk} from {DeliverySlot as ds} where {ds.warehouse} = ?warehouse and {ds.starttime} > ?start and {ds.endtime} < ?end and ({ds.available} - ({{select count(*) from {DeliverySlotManagement as dsm} where {dsm.deliveryslot}={ds.pk}}}))>0'

    public static final QUERY_ALL_SLOTS = 'select {pk} from {DeliverySlot as ds} where {ds.warehouse} = ?warehouse and {ds.starttime} > ?start and {ds.endtime} < ?end order by {ds.starttime}'

    public static final QUERY_GET_SLOT = 'select {pk} from {DeliverySlot as ds} where {ds.code} = ?slotCode and ({ds.available} - ({{select count(*) from {DeliverySlotManagement as dsm} where {dsm.deliveryslot}={ds.pk}}}))>0'

    public static final QUERY_GET_SLOT_MANAGEMENT = 'select {pk} from {DeliverySlotManagement as dsm} where {dsm.code} = ?slotManagementCode'

    public static final QUERY_GET_SLOT_MANAGEMENT_BY_CART = 'select {pk} from {DeliverySlotManagement as dsm join Cart as c on {dsm.abstractorder}={c.pk}} where {c.pk} = ?cart'

    @Resource
    private FlexibleSearchService flexibleSearchService

    @Resource
    private ModelService modelService

    @Override
    List<DeliverySlotModel> getAvailableDeliverySlots(Date start, Date end, WarehouseModel warehouse) {
        def result = flexibleSearchService.<DeliverySlotModel> search(QUERY_AVAILABLE_SLOTS, [warehouse: warehouse, start: start, end: end])
        result.getResult()
    }

    @Override
    List<DeliverySlotModel> getAllDeliverySlots(Date start, Date end, WarehouseModel warehouse) {
        def result = flexibleSearchService.<DeliverySlotModel> search(QUERY_ALL_SLOTS, [warehouse: warehouse, start: start, end: end])
        result.getResult()
    }

    @Override
    DeliverySlotManagementModel bookDelivery(String deliverySlotCode, CartModel cart) {
        DeliverySlotManagementModel deliverySlotManagement = getBookedSlot(cart)
        if (deliverySlotManagement) {
            LOG.info("Cart has already a booked slot associated")
            return null
        }
        def deliverySlot = findSlotByCode(deliverySlotCode)
        if (deliverySlot) {
            deliverySlotManagement = modelService.create(DeliverySlotManagementModel)
            deliverySlotManagement.setCode(deliverySlot.getCode() + "_" + UUID.randomUUID().toString())
            deliverySlotManagement.setAbstractorder(cart)
            deliverySlotManagement.setDeliveryslot(deliverySlot)
            deliverySlotManagement.setTimestamp(new Date())
            deliverySlotManagement.setStatus(DeliverySlotStatus.BOOKED)
            modelService.save(deliverySlotManagement)
        }
        deliverySlotManagement
    }

    @Override
    DeliverySlotManagementModel getBookedSlot(CartModel cart) {
        def result = flexibleSearchService.<DeliverySlotManagementModel> search(QUERY_GET_SLOT_MANAGEMENT_BY_CART, [cart: cart])
        result.getCount() > 0 ? result.getResult().get(0) : null
    }

    @Override
    DeliverySlotManagementModel confirmDelivery(String deliverySlotManagementCode, OrderModel order) {
        def deliverySlotManagement = findSlotManagementByCode(deliverySlotManagementCode)
        if (deliverySlotManagement && deliverySlotManagement.getStatus() == DeliverySlotStatus.BOOKED) {
            deliverySlotManagement.setAbstractorder(order)
            deliverySlotManagement.setTimestamp(new Date())
            deliverySlotManagement.setStatus(DeliverySlotStatus.CONFIRMED)
            modelService.save(deliverySlotManagement)
        }
        deliverySlotManagement
    }

    @Override
    DeliverySlotManagementModel changeDelivery(String deliverySlotCode, CartModel cart) {
        def deliverySlotManagement = getBookedSlot(cart)
        def deliverySlot = findSlotByCode(deliverySlotCode)
        if (deliverySlotManagement && deliverySlot && deliverySlotManagement.getStatus() == DeliverySlotStatus.BOOKED) {
            deliverySlotManagement.setTimestamp(new Date())
            deliverySlotManagement.setDeliveryslot(deliverySlot)
            modelService.save(deliverySlotManagement)
        } else {
            deliverySlotManagement = null
        }
        deliverySlotManagement
    }

    @Override
    boolean cancelDelivery(CartModel cart) {
        def deliverySlotManagement = getBookedSlot(cart)
        if (deliverySlotManagement && deliverySlotManagement.getStatus() == DeliverySlotStatus.BOOKED) {
            modelService.remove(deliverySlotManagement)
            return true
        }
        return false
    }

    @Override
    DeliverySlotModel findSlotByCode(String code) {
        def result = flexibleSearchService.<DeliverySlotModel> search(QUERY_GET_SLOT, [slotCode: code])
        result.getCount() > 0 ? result.getResult().get(0) : null
    }

    @Override
    DeliverySlotManagementModel findSlotManagementByCode(String code) {
        def result = flexibleSearchService.<DeliverySlotManagementModel> search(QUERY_GET_SLOT_MANAGEMENT, [slotManagementCode: code])
        result.getCount() > 0 ? result.getResult().get(0) : null
    }

}
