import com.sap.cx.boosters.easy.easytutorialstep5.controller.AvailableSlotsController
import com.sap.cx.boosters.easy.easytutorialstep5.controller.BookDeliveryController
import com.sap.cx.boosters.easy.easytutorialstep5.controller.CancelDeliveryController
import com.sap.cx.boosters.easy.easytutorialstep5.controller.ChangeDeliveryController
import com.sap.cx.boosters.easy.easytutorialstep5.controller.ConfirmDeliveryController
import com.sap.cx.boosters.easy.easytutorialstep5.controller.GetBookedDeliveryController
import com.sap.cx.boosters.easy.easytutorialstep5.service.impl.DefaultDeliverySlotService
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

    bookDeliveryController(BookDeliveryController)

    cancelDeliveryController(CancelDeliveryController)

    confirmDeliveryController(ConfirmDeliveryController)

    getBookedDeliveryController(GetBookedDeliveryController)

    changeDeliveryController(ChangeDeliveryController)

    logger.info "[${extension.id}] registered Spring core beans ..."
}

logger.info "[${extension.id}] registered [/easyrest] Spring beans."

