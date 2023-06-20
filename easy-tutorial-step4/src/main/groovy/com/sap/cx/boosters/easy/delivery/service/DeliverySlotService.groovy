package com.sap.cx.boosters.easy.delivery.service


import com.sap.cx.boosters.easy.delivery.easytype.model.DeliverySlotManagementModel
import com.sap.cx.boosters.easy.delivery.easytype.model.DeliverySlotModel
import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.core.model.order.OrderModel
import de.hybris.platform.enumeration.EnumerationService
import de.hybris.platform.ordersplitting.model.WarehouseModel
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.servicelayer.search.FlexibleSearchService

class DeliverySlotService {
    public static final QUERY_AVAILABLE_SLOTS = 'select {pk} from {DeliverySlot as ds} where {ds.warehouse} = ?warehouse and {ds.starttime} > ?start and {ds.endtime} < ?end and ({ds.available} - ({{select count(*) from {DeliverySlotManagement as dsm} where {dsm.deliveryslot}={ds.pk}}}))>0'
    public static final QUERY_ALL_SLOTS = 'select {pk} from {DeliverySlot as ds} where {ds.warehouse} = ?warehouse and {ds.starttime} > ?start and {ds.endtime} < ?end order by {ds.starttime}'
    public static final QUERY_GET_SLOT = 'select {pk} from {DeliverySlot as ds} where {ds.code} = ?slotCode and ({ds.available} - ({{select count(*) from {DeliverySlotManagement as dsm} where {dsm.deliveryslot}={ds.pk}}}))>0'
    public static final QUERY_GET_SLOT_MANAGEMENT = 'select {pk} from {DeliverySlotManagement as dsm} where {dsm.code} = ?slotManagementCode'
    public static final QUERY_GET_SLOT_MANAGEMENT_BY_CART = 'select {pk} from {DeliverySlotManagement as dsm join Cart as c on {dsm.abstractorder}={c.pk}} where {c.pk} = ?cart'


    def LOG = org.slf4j.LoggerFactory.getLogger("DeliverySlotService");

    def FlexibleSearchService flexibleSearchService
    def ModelService modelService
    def EnumerationService enumerationService


    List<DeliverySlotModel> getAvailableDeliverySlots(Date start, Date end, WarehouseModel warehouse){
        def result = flexibleSearchService.search(QUERY_AVAILABLE_SLOTS,[warehouse:warehouse, start:start, end:end])
        result.getResult()
    }

    List<DeliverySlotModel> getAllDeliverySlots(Date start, Date end, WarehouseModel warehouse){
        def result = flexibleSearchService.search(QUERY_ALL_SLOTS,[warehouse:warehouse, start:start, end:end])
        result.getResult()
    }

    def bookDelivery(String deliverySlotCode, CartModel cart){
        def deliverySlotManagement = getBookedSlot(cart)
        if (deliverySlotManagement){
            LOG.info("Cart has already a booked slot associated")
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

    def confirmDelivery(String deliverySlotManagementCode, OrderModel order){
        def deliverySlotManagement = findSlotManagementByCode(deliverySlotManagementCode)
        if (deliverySlotManagement && deliverySlotManagement.getStatus()==BOOKED()){
            deliverySlotManagement.setAbstractorder(order)
            deliverySlotManagement.setTimestamp(new Date())
            deliverySlotManagement.setStatus(CONFIRMED())
            modelService.save(deliverySlotManagement)
        }
        deliverySlotManagement
    }

    def changeDelivery(String deliverySlotCode, CartModel cart){
        def deliverySlotManagement = getBookedSlot(cart)
        def deliverySlot = findSlotByCode(deliverySlotCode)
        if (deliverySlotManagement && deliverySlot && deliverySlotManagement.getStatus()==BOOKED()){
            deliverySlotManagement.setTimestamp(new Date())
            deliverySlotManagement.setDeliveryslot(deliverySlot)
            modelService.save(deliverySlotManagement)
        }else{
            deliverySlotManagement = null
        }
        deliverySlotManagement
    }

    boolean cancelDelivery(CartModel cart){
        def deliverySlotManagement = getBookedSlot(cart)
        if (deliverySlotManagement && deliverySlotManagement.getStatus()==BOOKED()){
            modelService.remove(deliverySlotManagement)
            return true
        }
        return false
    }

    def findSlotByCode(String code){
        def result = flexibleSearchService.search(QUERY_GET_SLOT,[slotCode:code])
        result.getCount()>0?result.getResult().get(0):null
    }

    def findSlotManagementByCode(String code){
        def result = flexibleSearchService.search(QUERY_GET_SLOT_MANAGEMENT,[slotManagementCode:code])
        result.getCount()>0?result.getResult().get(0):null
    }

    def BOOKED(){
        enumerationService.getEnumerationValue("DeliverySlotStatus","BOOKED")
    }

    def CONFIRMED(){
        enumerationService.getEnumerationValue("DeliverySlotStatus","CONFIRMED")
    }
}
