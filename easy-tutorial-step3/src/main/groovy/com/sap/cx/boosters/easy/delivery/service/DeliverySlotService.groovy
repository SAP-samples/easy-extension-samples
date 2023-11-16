package com.sap.cx.boosters.easy.delivery.service

import com.sap.cx.boosters.easy.delivery.easytype.enums.DeliverySlotStatus
import com.sap.cx.boosters.easy.delivery.easytype.models.DeliverySlotManagementModel
import com.sap.cx.boosters.easy.delivery.easytype.models.DeliverySlotModel
import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.core.model.order.OrderModel
import de.hybris.platform.enumeration.EnumerationService
import de.hybris.platform.ordersplitting.model.WarehouseModel
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.servicelayer.search.FlexibleSearchService

import javax.annotation.PostConstruct
import javax.annotation.Resource

class DeliverySlotService {

    public static final QUERY_GET_SLOT = 'select {pk} from {DeliverySlot as ds} where {ds.code} = ?slotCode and ({ds.available} - ({{select count(*) from {DeliverySlotManagement as dsm} where {dsm.deliveryslot}={ds.pk}}}))>0'

    public static final QUERY_GET_SLOT_MANAGEMENT_BY_CART = 'select {pk} from {DeliverySlotManagement as dsm join Cart as c on {dsm.abstractorder}={c.pk}} where {c.pk} = ?cart'

    def LOG = org.slf4j.LoggerFactory.getLogger(DeliverySlotService)

    @Resource
    FlexibleSearchService flexibleSearchService

    @Resource
    ModelService modelService

    @Resource
    EnumerationService enumerationService

    public DeliverySlotStatus BOOKED

    public DeliverySlotStatus CONFIRMED

    @PostConstruct
    def init() {
        BOOKED = enumerationService.getEnumerationValue('DeliverySlotStatus','BOOKED')
        CONFIRMED = enumerationService.getEnumerationValue('DeliverySlotStatus','CONFIRMED')
    }

    List<DeliverySlotModel> getAvailableDeliverySlots(Date start, Date end, WarehouseModel warehouse){
        // TODO Should return the list of available slots for the given Warehouse and start and end date. To find available slots we need to subtract
        // from "DeliverySlot.available" matching row cont in "DeliverySlotManagement".
        // Only DeliverySlots with:
        //select {pk} from {DeliverySlot as ds} where {ds.warehouse} = ?warehouse and {ds.starttime} > ?start and {ds.endtime} < ?end and ({ds.available} - ({{select count(*) from {DeliverySlotManagement as dsm} where {dsm.deliveryslot}={ds.pk}}}))>0
        //has to be returned
    }

    List<DeliverySlotModel> getAllDeliverySlots(Date start, Date end, WarehouseModel warehouse){
        //TODO Should return the list of all slots for the given Warehouse and start and end date
    }

    def confirmDelivery(String deliverySlotManagementCode, OrderModel order){
        // TODO The operation retrieve the deliveryslotmanagement item given the passed code parameter, check if the item is in the BOOKED status and then perform the update,
        // setting the order in place of the cart and updating the status to CONFIRMED.
    }

    def changeDelivery(String deliverySlotCode, CartModel cart){
        // TODO This operation is used when a customer change the delivery slot previously booked.
        //The service retrieves the deliverySlotManagement associated to the passed cart and if it exists and is still in the BOOKED status and if the passed delivery slot
        // is still available, then it performs the update
    }

    def bookDelivery(String deliverySlotCode, CartModel cart){
        def deliverySlotManagement = getBookedSlot(cart)
        if (deliverySlotManagement){
            LOG.info 'Cart has already a booked slot associated'
            return null
        }
        def deliverySlot = findSlotByCode(deliverySlotCode)
        if (deliverySlot){
            deliverySlotManagement = modelService.create(DeliverySlotManagementModel._TYPECODE)
            deliverySlotManagement.setCode(deliverySlot.getCode()+"_"+ UUID.randomUUID().toString())
            deliverySlotManagement.setAbstractorder(cart)
            deliverySlotManagement.setDeliveryslot(deliverySlot)
            deliverySlotManagement.setTimestamp(new Date())
            deliverySlotManagement.setStatus(BOOKED())
            modelService.save(deliverySlotManagement)
        }
        deliverySlotManagement
    }

    def getBookedSlot(CartModel cart){
        def result = flexibleSearchService.search(QUERY_GET_SLOT_MANAGEMENT_BY_CART,[cart:cart])
        result.getCount()>0?result.getResult().get(0):null
    }

    def findSlotByCode(String code){
        def result = flexibleSearchService.search(QUERY_GET_SLOT,[slotCode:code])
        result.getCount()>0?result.getResult().get(0):null
    }

}
