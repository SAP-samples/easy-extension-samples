logger.info "[${extension.id}] registering Spring beans for ..."

easyCoreBeans {
	deliverySlotService(com.sap.cx.boosters.easy.delivery.service.DeliverySlotService)
}

println "Registering easyrest beans for ${extension.id}"

easyWebBeans('/easyrest') {

}

logger.info "[${extension.id}] beans registered ..."