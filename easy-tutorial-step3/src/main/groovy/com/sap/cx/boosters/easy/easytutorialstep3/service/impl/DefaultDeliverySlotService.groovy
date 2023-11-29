package com.sap.cx.boosters.easy.easytutorialstep3.service.impl

import com.sap.cx.boosters.easy.easytutorialstep3.enums.DeliverySlotStatus
import com.sap.cx.boosters.easy.easytutorialstep3.models.DeliverySlotManagementModel
import com.sap.cx.boosters.easy.easytutorialstep3.models.DeliverySlotModel
import com.sap.cx.boosters.easy.easytutorialstep3.service.DeliverySlotService
import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.core.model.order.OrderModel
import de.hybris.platform.ordersplitting.model.WarehouseModel
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import org.slf4j.LoggerFactory

import javax.annotation.Resource

class DefaultDeliverySlotService implements DeliverySlotService {

    private static final def LOG = LoggerFactory.getLogger(DefaultDeliverySlotService)

    public static final QUERY_GET_SLOT = 'select {pk} from {DeliverySlot as ds} where {ds.code} = ?slotCode and ({ds.available} - ({{select count(*) from {DeliverySlotManagement as dsm} where {dsm.deliveryslot}={ds.pk}}}))>0'

    public static final QUERY_GET_SLOT_MANAGEMENT_BY_CART = 'select {pk} from {DeliverySlotManagement as dsm join Cart as c on {dsm.abstractorder}={c.pk}} where {c.pk} = ?cart'

    @Resource
    private FlexibleSearchService flexibleSearchService

    @Resource
    private ModelService modelService

    @Override
    List<DeliverySlotModel> getAvailableDeliverySlots(Date start, Date end, WarehouseModel warehouse) {
        // TODO Should return the list of available slots for the given Warehouse and start and end date. To find available slots we need to subtract
        // from "DeliverySlot.available" matching row cont in "DeliverySlotManagement".
        // Only DeliverySlots with:
        //select {pk} from {DeliverySlot as ds} where {ds.warehouse} = ?warehouse and {ds.starttime} > ?start and {ds.endtime} < ?end and ({ds.available} - ({{select count(*) from {DeliverySlotManagement as dsm} where {dsm.deliveryslot}={ds.pk}}}))>0
        //has to be returned
        return null
    }

    @Override
    List<DeliverySlotModel> getAllDeliverySlots(Date start, Date end, WarehouseModel warehouse) {
        //TODO Should return the list of all slots for the given Warehouse and start and end date
        return null
    }

    @Override
    DeliverySlotManagementModel confirmDelivery(String deliverySlotManagementCode, OrderModel order) {
        // TODO The operation retrieve the deliveryslotmanagement item given the passed code parameter, check if the item is in the BOOKED status and then perform the update,
        // setting the order in place of the cart and updating the status to CONFIRMED.
        return null
    }

    @Override
    DeliverySlotManagementModel changeDelivery(String deliverySlotCode, CartModel cart) {
        // TODO This operation is used when a customer change the delivery slot previously booked.
        //The service retrieves the deliverySlotManagement associated to the passed cart and if it exists and is still in the BOOKED status and if the passed delivery slot
        // is still available, then it performs the update
        return null
    }

    @Override
    DeliverySlotManagementModel bookDelivery(String deliverySlotCode, CartModel cart) {
        DeliverySlotManagementModel deliverySlotManagement = getBookedSlot(cart)
        if (null != deliverySlotManagement) {
            LOG.info("Cart has already a booked slot associated")
            return null
        }
        def deliverySlot = findSlotByCode(deliverySlotCode)
        if (deliverySlot) {
            deliverySlotManagement = modelService.create(DeliverySlotManagementModel)
            deliverySlotManagement.code = deliverySlot.getCode() + "_" + UUID.randomUUID().toString()
            deliverySlotManagement.setAbstractorder(cart)
            deliverySlotManagement.setDeliveryslot(deliverySlot)
            deliverySlotManagement.setTimestamp(new Date())
            deliverySlotManagement.setStatus(DeliverySlotStatus.BOOKED)
            modelService.save(deliverySlotManagement)
        }
        return deliverySlotManagement
    }

    @Override
    DeliverySlotManagementModel getBookedSlot(CartModel cart) {
        def result = flexibleSearchService.<DeliverySlotManagementModel> search(QUERY_GET_SLOT_MANAGEMENT_BY_CART, [cart: cart])
        return result.getCount() > 0 ? result.getResult().get(0) : null
    }

    @Override
    DeliverySlotModel findSlotByCode(String code) {
        def result = flexibleSearchService.<DeliverySlotModel> search(QUERY_GET_SLOT, [slotCode: code])
        return result.getCount() > 0 ? result.getResult().get(0) : null
    }

}
