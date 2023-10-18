package com.sap.cx.boosters.easy.delivery.service

import com.sap.cx.boosters.easy.delivery.easytype.enums.DeliverySlotStatus
import com.sap.cx.boosters.easy.delivery.easytype.model.DeliverySlotManagementModel
import com.sap.cx.boosters.easy.delivery.easytype.model.DeliverySlotModel
import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.core.model.order.OrderModel
import de.hybris.platform.enumeration.EnumerationService
import de.hybris.platform.ordersplitting.model.WarehouseModel
import de.hybris.platform.servicelayer.model.ModelService
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import org.springframework.beans.factory.annotation.Autowired

import javax.annotation.PostConstruct
import javax.annotation.Resource

class DeliverySlotService {

    public static final QUERY_AVAILABLE_SLOTS = 'select {pk} from {DeliverySlot as ds} where {ds.warehouse} = ?warehouse and {ds.starttime} > ?start and {ds.endtime} < ?end and ({ds.available} - ({{select count(*) from {DeliverySlotManagement as dsm} where {dsm.deliveryslot}={ds.pk}}})) > 0'
    public static final QUERY_ALL_SLOTS = 'select {pk} from {DeliverySlot as ds} where {ds.warehouse} = ?warehouse and {ds.starttime} > ?start and {ds.endtime} < ?end order by {ds.starttime}'
    public static final QUERY_GET_SLOT = 'select {pk} from {DeliverySlot as ds} where {ds.code} = ?slotCode and ({ds.available} - ({{select count(*) from {DeliverySlotManagement as dsm} where {dsm.deliveryslot}={ds.pk}}})) > 0'
    public static final QUERY_GET_SLOT_MANAGEMENT = 'select {pk} from {DeliverySlotManagement as dsm} where {dsm.code} = ?slotManagementCode'
    public static final QUERY_GET_SLOT_MANAGEMENT_BY_CART = 'select {pk} from {DeliverySlotManagement as dsm join Cart as c on {dsm.abstractorder}={c.pk}} where {c.pk} = ?cart'

    def LOG = org.slf4j.LoggerFactory.getLogger('DeliverySlotService')

    @Resource
    FlexibleSearchService flexibleSearchService

    @Resource
    ModelService modelService

    @Resource
    EnumerationService enumerationService

    // DeliverySlotStatus BOOKED

    // DeliverySlotStatus CONFIRMED

    @PostConstruct
    def init() {

        /*
        def classLoaderHierarchy = {ClassLoader cl ->
            def pad = 0
            while (cl) {
                println((' '* pad) + "${cl} [${cl.class.name}]")
                cl = cl.parent
                pad++
            }
        }

        def _BOOKED = enumerationService.getEnumerationValue('DeliverySlotStatus','BOOKED')
        def _CONFIRMED = enumerationService.getEnumerationValue('DeliverySlotStatus','CONFIRMED')
        println ">>> ${_BOOKED} [${_BOOKED.class}]"
        classLoaderHierarchy(_BOOKED.class.classLoader)
        println ">>> ${_CONFIRMED} [${_CONFIRMED.class}]"
        classLoaderHierarchy(_CONFIRMED.class.classLoader)
        println ">>> ${DeliverySlotStatus} [${DeliverySlotStatus.classLoader}]"
        classLoaderHierarchy(DeliverySlotStatus.classLoader)
        */

        // BOOKED = enumerationService.getEnumerationValue('DeliverySlotStatus','BOOKED')
        // CONFIRMED = enumerationService.getEnumerationValue('DeliverySlotStatus','CONFIRMED')

    }

    List<DeliverySlotModel> getAvailableDeliverySlots(Date start, Date end, WarehouseModel warehouse) {
        def result = flexibleSearchService.search(QUERY_AVAILABLE_SLOTS, [warehouse:warehouse, start:start, end:end])
        result.getResult()
    }

    List<DeliverySlotModel> getAllDeliverySlots(Date start, Date end, WarehouseModel warehouse){
        def searchResult = flexibleSearchService.search(QUERY_ALL_SLOTS, [warehouse:warehouse, start:start, end:end])
        searchResult.result
    }

    DeliverySlotManagementModel bookDelivery(String deliverySlotCode, CartModel cart) {

        def deliverySlotManagement = getBookedSlot(cart)
        if (deliverySlotManagement){
            LOG.info 'Cart has already a booked slot associated'
            return null
        }

        def deliverySlot = findSlotByCode(deliverySlotCode)
        if (deliverySlot){
            deliverySlotManagement = modelService.create(DeliverySlotManagementModel._TYPECODE) as DeliverySlotManagementModel
            deliverySlotManagement.code = "${deliverySlot.code}_${UUID.randomUUID()}"
            deliverySlotManagement.abstractorder = cart
            deliverySlotManagement.deliveryslot = deliverySlot
            deliverySlotManagement.timestamp = new Date()
            deliverySlotManagement.status = BOOKED
            modelService.save(deliverySlotManagement)
        }
        deliverySlotManagement

    }

    DeliverySlotManagementModel getBookedSlot(CartModel cart) {
        def searchResult = flexibleSearchService.search(QUERY_GET_SLOT_MANAGEMENT_BY_CART, [cart:cart])
        searchResult.count > 0 ? searchResult.result.get(0) : null
    }

    DeliverySlotManagementModel confirmDelivery(String deliverySlotManagementCode, OrderModel order) {
        def deliverySlotManagement = findSlotManagementByCode(deliverySlotManagementCode)
        if (deliverySlotManagement && deliverySlotManagement.status == BOOKED){
            deliverySlotManagement.abstractorder = order
            deliverySlotManagement.timestamp = new Date()
            deliverySlotManagement.status = CONFIRMED
            modelService.save(deliverySlotManagement)
        }
        deliverySlotManagement
    }

    DeliverySlotManagementModel changeDelivery(String deliverySlotCode, CartModel cart) {
        def deliverySlotManagement = getBookedSlot(cart)
        def deliverySlot = findSlotByCode(deliverySlotCode)
        if (deliverySlotManagement && deliverySlot && deliverySlotManagement.status == BOOKED) {
            deliverySlotManagement.timestamp = new Date()
            deliverySlotManagement.deliveryslot = deliverySlot
            modelService.save(deliverySlotManagement)
        } else {
            deliverySlotManagement = null
        }
        deliverySlotManagement
    }

    boolean cancelDelivery(CartModel cart) {
        def deliverySlotManagement = getBookedSlot(cart)
        if (deliverySlotManagement && deliverySlotManagement.getStatus() == BOOKED) {
            modelService.remove(deliverySlotManagement)
            return true
        }
        return false
    }

    DeliverySlotModel findSlotByCode(String code) {
        def result = flexibleSearchService.search(QUERY_GET_SLOT, [slotCode:code])
        result.getCount()>0?result.getResult().get(0):null
    }

    DeliverySlotManagementModel findSlotManagementByCode(String code) {
        def searchResult = flexibleSearchService.search(QUERY_GET_SLOT_MANAGEMENT, [slotManagementCode:code])
        searchResult.count > 0 ? searchResult.result.get(0) : null
    }

    def getBOOKED(){
        enumerationService.getEnumerationValue('DeliverySlotStatus','BOOKED')
    }

    def getCONFIRMED(){
        enumerationService.getEnumerationValue('DeliverySlotStatus','CONFIRMED')
    }

}
