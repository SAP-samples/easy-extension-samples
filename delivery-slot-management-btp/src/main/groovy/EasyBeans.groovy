import com.sap.cx.boosters.easy.deliverybtp.controller.*
import com.sap.cx.boosters.easy.deliverybtp.placeorderhook.HomeDeliveryCommercePlaceOrderMethodHookBtp
import com.sap.cx.boosters.easy.deliverybtp.populators.HomeDeliveryModeOrderPopulatorBtp
import com.sap.cx.boosters.easy.deliverybtp.service.DeliverySlotCleanupServiceBtp
import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao

logger.info "[${extension.id}] registering Spring beans ..."

easyCoreBeans {
    defaultCartGenericDao(DefaultGenericDao, CartModel._TYPECODE)

    deliverySlotCleanupServiceBtp(DeliverySlotCleanupServiceBtp)

    homeDeliveryCommercePlaceOrderMethodHookBtp(HomeDeliveryCommercePlaceOrderMethodHookBtp)

    homeDeliveryModeOrderPopulatorBtp(HomeDeliveryModeOrderPopulatorBtp)

    logger.info "[${extension.id}] registered Spring core beans."

}

logger.info "[${extension.id}] updating 'commercePlaceOrderMethodHooks'  ..."

commercePlaceOrderMethodHooks.removeAll { it.getClass().getSimpleName().equalsIgnoreCase("HomeDeliveryCommercePlaceOrderMethodHookBtp") }
commercePlaceOrderMethodHooks.add(homeDeliveryCommercePlaceOrderMethodHookBtp)

logger.info "[${extension.id}] updated 'commercePlaceOrderMethodHooks'  ..."

logger.info "[${extension.id}] updating order populator for the home delivery mode.. "

orderConverter.getPopulators().removeAll { it.getClass().getSimpleName().equalsIgnoreCase("HomeDeliveryModeOrderPopulatorBtp") }
orderConverter.getPopulators().add(homeDeliveryModeOrderPopulatorBtp)

logger.info "[${extension.id}] updated order populator for the home delivery mode.. "


easyWebBeans('/easyrest') {
    logger.info "[${extension.id}] registering [/easyrest] Spring beans ..."
    availableSlotsControllerBtp(AvailableSlotsControllerBtp)

    bookDeliveryControllerBtp(BookDeliveryControllerBtp)

    getBookedDeliveryControllerBtp(GetBookedDeliveryControllerBtp)

    changeDeliveryControllerBtp(ChangeDeliveryControllerBtp)

    confirmDeliveryControllerBtp(ConfirmDeliveryControllerBtp)

    logger.info "[${extension.id}] registered Spring core beans ..."
}


logger.info "[${extension.id}] registered [/easyrest] Spring beans."
