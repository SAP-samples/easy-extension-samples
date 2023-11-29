import com.sap.cx.boosters.easy.easytutorialstep4.controller.AvailableSlotsController
import com.sap.cx.boosters.easy.easytutorialstep4.service.impl.DefaultDeliverySlotService
import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao

logger.info "[${extension.id}] registering Spring beans ..."

easyCoreBeans {
	logger.info "[${extension.id}] registering Spring core beans ..."

	deliverySlotService(DefaultDeliverySlotService)

	defaultCartGenericDao(DefaultGenericDao, CartModel._TYPECODE)

	logger.info "[${extension.id}] registered Spring core beans."

}

easyWebBeans('/easyrest') {
	logger.info "[${extension.id}] registering [/easyrest] Spring beans ..."

	availableSlotsController(AvailableSlotsController)

	logger.info "[${extension.id}] registered Spring core beans ..."
}

logger.info "[${extension.id}] registered [/easyrest] Spring beans."

