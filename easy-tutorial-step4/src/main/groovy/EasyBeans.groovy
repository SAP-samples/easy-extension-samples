logger.info "[${extension.id}] registering Spring beans for ..."

easyCoreBeans {
	deliverySlotService(com.sap.cx.boosters.easy.delivery.service.DeliverySlotService)
	defaultCartGenericDao(de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao, 'Cart')
}

println "Registering easyrest beans for ${extension.id}"

easyWebBeans('/easyrest') {
	availableSlotsController(com.sap.cx.boosters.easy.delivery.controller.AvailableSlotsController)
}

logger.info "[${extension.id}] beans registered ..."
