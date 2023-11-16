logger.info "[${extension.id}] registering Spring beans for ..."

easyCoreBeans {
	deliverySlotService(com.sap.cx.boosters.easy.delivery.service.DeliverySlotService)
	defaultCartGenericDao(de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao, 'Cart')
}

println "Registering beans on Web Application Context: " + spring

easyWebBeans('/easyrest') {
	availableSlotsController(com.sap.cx.boosters.easy.delivery.controller.AvailableSlotsController)
	bookDeliveryController(com.sap.cx.boosters.easy.delivery.controller.BookDeliveryController)
	cancelDeliveryController(com.sap.cx.boosters.easy.delivery.controller.CancelDeliveryController)
	confirmDeliveryController(com.sap.cx.boosters.easy.delivery.controller.ConfirmDeliveryController)
	getBookedDeliveryController(com.sap.cx.boosters.easy.delivery.controller.GetBookedDeliveryController)
	changeDeliveryController(com.sap.cx.boosters.easy.delivery.controller.ChangeDeliveryController)
}

logger.info "[${extension.id}] beans registered ..."
