package com.sap.cx.boosters.easy.deliverybtp.placeorderhook


import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.apiregistryservices.services.DestinationService
import de.hybris.platform.commerceservices.enums.CustomerType
import de.hybris.platform.commerceservices.order.hook.CommercePlaceOrderMethodHook
import de.hybris.platform.commerceservices.service.data.CommerceCheckoutParameter
import de.hybris.platform.commerceservices.service.data.CommerceOrderResult
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartException
import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.core.model.order.OrderModel
import de.hybris.platform.core.model.user.CustomerModel
import de.hybris.platform.order.InvalidCartException
import de.hybris.platform.outboundservices.client.IntegrationRestTemplateFactory
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class HomeDeliveryCommercePlaceOrderMethodHookBtp implements CommercePlaceOrderMethodHook {

    public static final HOME_DELIVERY_MODE = "homedelivery"

    public static final DSM_DESTINATION_ID = "dsmBtpDestination"
    public static final DSM_DESTINATION_TARGET_ID = "dsmBtpDestinationTarget"
    private static final GET_BOOKED_SLOT_URL = "/api/deliveryslotmanagements/getBookedSlot?cartCode={cartCode}"
    private static final CONFIRM_SLOT_URL = "/api/deliveryslotmanagements/confirmDelivery?deliverySlotManagementCode={deliverySlotManagementCode}&orderCode={orderCode}"

    def LOG = org.slf4j.LoggerFactory.getLogger("HomeDeliveryCommercePlaceOrderMethodHookBtp");

    DestinationService<ConsumedDestinationModel> destinationService
    IntegrationRestTemplateFactory integrationRestTemplateFactory

    @Override
    void afterPlaceOrder(CommerceCheckoutParameter parameter, CommerceOrderResult orderModel) throws InvalidCartException {
        if (parameter.getCart().getDeliveryMode().getCode().startsWith(HOME_DELIVERY_MODE)) {
            def btpResponse = getBookedSlot(parameter.getCart())
            if (btpResponse.getStatusCode() != HttpStatus.OK) {
                LOG.error("No delivery slot management found! Order has been created, customer support need to solve the issue")
            } else {
                String bookedSlotCode = btpResponse.getBody().get("code")
                btpResponse = confirmBookedSlot(orderModel.getOrder(), bookedSlotCode);
                if (btpResponse.getStatusCode() != HttpStatus.OK) {
                    LOG.error("Booking confirmation failed! Order has been created, customer support need to solve the issue")
                }
            }
        }
    }

    @Override
    void beforePlaceOrder(CommerceCheckoutParameter parameter) throws InvalidCartException {
        if (parameter.getCart().getDeliveryMode().getCode().startsWith(HOME_DELIVERY_MODE)) {
            def btpResponse = getBookedSlot(parameter.getCart())
            if (btpResponse.getStatusCode() != HttpStatus.OK) {
                throw new CartException("No delivery slot has been selected for the home delivery. Go back to the Home Delivery slot booking checkout step!", CartException.INVALID)
            }
        }
    }

    @Override
    void beforeSubmitOrder(CommerceCheckoutParameter parameter, CommerceOrderResult result) throws InvalidCartException {

    }

    protected ResponseEntity<?> getBookedSlot(CartModel cart) {
        def customer = cart.getUser()
        def cartCode
        if (customer instanceof CustomerModel && CustomerType.REGISTERED.equals(((CustomerModel) customer).getType())) {
            cartCode = cart.getCode()
        } else {
            cartCode = cart.getGuid()
        }
        return getBtpResponse(HttpMethod.GET, GET_BOOKED_SLOT_URL, ["cartCode": cartCode])
    }

    protected ResponseEntity<?> confirmBookedSlot(OrderModel order, String deliverySlotManagementCode) {
        return getBtpResponse(HttpMethod.PUT, CONFIRM_SLOT_URL, ["deliverySlotManagementCode": deliverySlotManagementCode,"orderCode": order.getCode()])
    }

    protected ResponseEntity<?> getBtpResponse(HttpMethod method, String restUrl, Map uriVariables) {
        def destination = destinationService.getDestinationByIdAndByDestinationTargetId(DSM_DESTINATION_ID, DSM_DESTINATION_TARGET_ID)
        if (destination == null) {
            throw new CartException("No destination found for Btp microservice")
        }

        def restOperations = integrationRestTemplateFactory.create(destination)
        def url = "$destination.url$restUrl"

        def btpResponse
        try {

            switch (method) {
                case HttpMethod.GET:
                    btpResponse = restOperations.getForEntity(url, Object.class, uriVariables)
                    break
                case HttpMethod.PUT:
                    btpResponse = restOperations.exchange(url, HttpMethod.PUT, null, Object.class, uriVariables)
                    break
            }
        }
        catch (org.springframework.web.client.RestClientResponseException e) {
            btpResponse = new ResponseEntity<>(e.getResponseBodyAsString(),HttpStatus.valueOf(e.getRawStatusCode()))
        }

        return btpResponse
    }
}
