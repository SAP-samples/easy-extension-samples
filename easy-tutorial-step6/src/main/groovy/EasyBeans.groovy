logger.info "[${extension.id}] registering Spring beans for ..."

easyCoreBeans {
	deliverySlotService(com.sap.cx.boosters.easy.delivery.service.DeliverySlotService)
	deliverySlotCleanupService(com.sap.cx.boosters.easy.delivery.service.DeliverySlotCleanupService)
	defaultCartGenericDao(de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao, 'Cart')
	homeDeliveryCommercePlaceOrderMethodHook(com.sap.cx.boosters.easy.delivery.placeorderhook.HomeDeliveryCommercePlaceOrderMethodHook)
	deliverySlotManagementAttributeHandler(com.sap.cx.boosters.easy.delivery.handler.DeliverySlotManagementAttributeHandler)
	homeDeliveryModeOrderPopulator(com.sap.cx.boosters.easy.delivery.populators.HomeDeliveryModeOrderPopulator)
}

println 'Adding the CommercePlaceOrderHook:'
commercePlaceOrderMethodHooks.removeAll { it.getClass().simpleName.equalsIgnoreCase('HomeDeliveryCommercePlaceOrderMethodHook')}
commercePlaceOrderMethodHooks.add(homeDeliveryCommercePlaceOrderMethodHook)

println 'Modifying order populator for the home delivery mode:'
orderConverter.populators.removeAll { it.getClass().simpleName.equalsIgnoreCase('HomeDeliveryModeOrderPopulator')}
orderConverter.populators.add(homeDeliveryModeOrderPopulator)

println "Registering easyrest beans for ${extension.id}"

easyWebBeans('/easyrest') {
	availableSlotsController(com.sap.cx.boosters.easy.delivery.controller.AvailableSlotsController)
	bookDeliveryController(com.sap.cx.boosters.easy.delivery.controller.BookDeliveryController)
	cancelDeliveryController(com.sap.cx.boosters.easy.delivery.controller.CancelDeliveryController)
	confirmDeliveryController(com.sap.cx.boosters.easy.delivery.controller.ConfirmDeliveryController)
	getBookedDeliveryController(com.sap.cx.boosters.easy.delivery.controller.GetBookedDeliveryController)
	changeDeliveryController(com.sap.cx.boosters.easy.delivery.controller.ChangeDeliveryController)
}

logger.info "[${extension.id}] beans registered ..."



