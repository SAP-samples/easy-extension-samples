import com.sap.cx.boosters.easy.deliveryslotmanagement.controller.AvailableSlotsController
import com.sap.cx.boosters.easy.deliveryslotmanagement.controller.BookDeliveryController
import com.sap.cx.boosters.easy.deliveryslotmanagement.controller.CancelDeliveryController
import com.sap.cx.boosters.easy.deliveryslotmanagement.controller.ChangeDeliveryController
import com.sap.cx.boosters.easy.deliveryslotmanagement.controller.ConfirmDeliveryController
import com.sap.cx.boosters.easy.deliveryslotmanagement.controller.GetBookedDeliveryController
import com.sap.cx.boosters.easy.deliveryslotmanagement.handler.DeliverySlotManagementAttributeHandler
import com.sap.cx.boosters.easy.deliveryslotmanagement.placeorderhook.HomeDeliveryCommercePlaceOrderMethodHook
import com.sap.cx.boosters.easy.deliveryslotmanagement.populators.HomeDeliveryModeOrderPopulator
import com.sap.cx.boosters.easy.deliveryslotmanagement.service.impl.DefaultDeliverySlotCleanupService
import com.sap.cx.boosters.easy.deliveryslotmanagement.service.impl.DefaultDeliverySlotService
import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao

logger.info "[${extension.id}] registering Spring beans ..."

easyCoreBeans {
    logger.info "[${extension.id}] registering Spring core beans ..."

    deliverySlotService(DefaultDeliverySlotService)

    deliverySlotCleanupService(DefaultDeliverySlotCleanupService)

    defaultCartGenericDao(DefaultGenericDao, CartModel._TYPECODE)

    homeDeliveryCommercePlaceOrderMethodHook(HomeDeliveryCommercePlaceOrderMethodHook)

    deliverySlotManagementAttributeHandler(DeliverySlotManagementAttributeHandler)

    homeDeliveryModeOrderPopulator(HomeDeliveryModeOrderPopulator)

    logger.info "[${extension.id}] registered Spring core beans."

}

logger.info "[${extension.id}] updating 'commercePlaceOrderMethodHooks'  ..."

commercePlaceOrderMethodHooks.removeAll { it.getClass().getSimpleName().equalsIgnoreCase("HomeDeliveryCommercePlaceOrderMethodHook") }
commercePlaceOrderMethodHooks.add(homeDeliveryCommercePlaceOrderMethodHook)

logger.info "[${extension.id}] updated 'commercePlaceOrderMethodHooks'  ..."


logger.info "[${extension.id}] updating order populator for the home delivery mode.. "

orderConverter.getPopulators().removeAll { it.getClass().getSimpleName().equalsIgnoreCase("HomeDeliveryModeOrderPopulator") }
orderConverter.getPopulators().add(homeDeliveryModeOrderPopulator)

logger.info "[${extension.id}] updated order populator for the home delivery mode.. "


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
