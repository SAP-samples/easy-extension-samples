import com.sap.cx.boosters.easy.delivery.handler.DeliverySlotManagementAttributeHandler
import com.sap.cx.boosters.easy.delivery.placeorderhook.HomeDeliveryCommercePlaceOrderMethodHook
import com.sap.cx.boosters.easy.delivery.populators.HomeDeliveryModeOrderPopulator
import com.sap.cx.boosters.easy.delivery.service.DeliverySlotCleanupService
import com.sap.cx.boosters.easy.delivery.service.DeliverySlotService
import com.sap.cx.boosters.easy.delivery.service.EasyEnhancedWarehouseService
import com.sap.cx.boosters.easy.delivery.service.EasyWarehouseService
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao

logger.info "[${extension.id}] registering Spring beans for ..."

easyCoreBeans {

	deliverySlotService(DeliverySlotService)
	deliverySlotCleanupService(DeliverySlotCleanupService)
	defaultCartGenericDao(DefaultGenericDao, 'Cart')
	homeDeliveryCommercePlaceOrderMethodHook(HomeDeliveryCommercePlaceOrderMethodHook)
	deliverySlotManagementAttributeHandler(DeliverySlotManagementAttributeHandler)
	homeDeliveryModeOrderPopulator(HomeDeliveryModeOrderPopulator)

	easyWarehouseService(EasyWarehouseService)
	easyEnhancedWarehouseService(EasyEnhancedWarehouseService)
	registerAlias('easyEnhancedWarehouseService', 'warehouseService')

}

/*
println 'Adding the CommercePlaceOrderHook:'
commercePlaceOrderMethodHooks.removeAll { it.getClass().simpleName.equalsIgnoreCase('HomeDeliveryCommercePlaceOrderMethodHook')}
commercePlaceOrderMethodHooks.add(homeDeliveryCommercePlaceOrderMethodHook)

println 'Modifying order populator for the home delivery mode:'
orderConverter.populators.removeAll { it.getClass().simpleName.equalsIgnoreCase('HomeDeliveryModeOrderPopulator')}
orderConverter.populators.add(homeDeliveryModeOrderPopulator)
*/

println "Registering REST beans for ${extension.id}"

easyWebBeans('/easyrest') {

	availableSlotsController(com.sap.cx.boosters.easy.delivery.controller.AvailableSlotsController)
	bookDeliveryController(com.sap.cx.boosters.easy.delivery.controller.BookDeliveryController)
	cancelDeliveryController(com.sap.cx.boosters.easy.delivery.controller.CancelDeliveryController)
	confirmDeliveryController(com.sap.cx.boosters.easy.delivery.controller.ConfirmDeliveryController)
	getBookedDeliveryController(com.sap.cx.boosters.easy.delivery.controller.GetBookedDeliveryController)
	changeDeliveryController(com.sap.cx.boosters.easy.delivery.controller.ChangeDeliveryController)

}

logger.info "[${extension.id}] beans registered ..."
