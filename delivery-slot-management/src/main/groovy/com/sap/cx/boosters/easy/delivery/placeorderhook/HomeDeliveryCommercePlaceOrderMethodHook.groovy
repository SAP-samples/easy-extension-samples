package com.sap.cx.boosters.easy.delivery.placeorderhook

import com.sap.cx.boosters.easy.delivery.service.DeliverySlotService
import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartException
import de.hybris.platform.order.InvalidCartException

class HomeDeliveryCommercePlaceOrderMethodHook implements CommercePlaceOrderMethodHook {

    public static final HOME_DELIVERY_MODE = "homedelivery"
    def DeliverySlotService deliverySlotService
    def LOG = org.slf4j.LoggerFactory.getLogger("HomeDeliveryCommercePlaceOrderMethodHook");

    @Override
    void afterPlaceOrder(CommerceCheckoutParameter parameter, CommerceOrderResult orderModel) throws InvalidCartException {
        if (parameter.getCart().getDeliveryMode().getCode().startsWith(HOME_DELIVERY_MODE)) {
            def slotManagement = deliverySlotService.getBookedSlot(parameter.getCart())
            if (!slotManagement) {
                LOG.error("No delivery slot management found! Order has been created, customer support need to solve the issue")
            } else {
                deliverySlotService.confirmDelivery(slotManagement.getCode(), orderModel.getOrder())
            }
        }
    }

    @Override
    void beforePlaceOrder(CommerceCheckoutParameter parameter) throws InvalidCartException {
        if (parameter.getCart().getDeliveryMode().getCode().startsWith(HOME_DELIVERY_MODE)) {
            def slotManagement = deliverySlotService.getBookedSlot(parameter.getCart())
            if (!slotManagement) {
                throw new CartException("No delivery slot has been selected for the home delivery. Go back to the Home Delivery slot booking checkout step!", CartException.INVALID)
            }
        }
    }

    @Override
    void beforeSubmitOrder(CommerceCheckoutParameter parameter, CommerceOrderResult result) throws InvalidCartException {

    }
}
