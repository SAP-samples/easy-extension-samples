import com.sap.cx.boosters.easy.easytutorialstep3.service.impl.DefaultDeliverySlotService

logger.info "[${extension.id}] registering Spring beans ..."

easyCoreBeans {
    logger.info "[${extension.id}] registering Spring core beans ..."

    deliverySlotService(DefaultDeliverySlotService)

    logger.info "[${extension.id}] registered Spring core beans."

}

easyWebBeans('/easyrest') {
    logger.info "[${extension.id}] registering [/easyrest] Spring beans ..."

    logger.info "[${extension.id}] registered Spring core beans ..."
}

logger.info "[${extension.id}] registered [/easyrest] Spring beans."

