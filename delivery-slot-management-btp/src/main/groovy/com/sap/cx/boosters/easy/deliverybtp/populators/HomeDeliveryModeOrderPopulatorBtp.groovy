package com.sap.cx.boosters.easy.deliverybtp.populators

import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.apiregistryservices.services.DestinationService
import de.hybris.platform.commercefacades.order.data.OrderData
import de.hybris.platform.commercewebservicescommons.errors.exceptions.CartException
import de.hybris.platform.converters.Populator
import de.hybris.platform.core.model.order.OrderModel
import de.hybris.platform.outboundservices.client.IntegrationRestTemplateFactory
import de.hybris.platform.servicelayer.dto.converter.ConversionException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestClientResponseException

import javax.annotation.Resource

class HomeDeliveryModeOrderPopulatorBtp implements Populator<OrderModel, OrderData> {

    private static final def LOG = LoggerFactory.getLogger(HomeDeliveryModeOrderPopulatorBtp)

    public static final DSM_DESTINATION_ID = "dsmBtpDestination"

    public static final DSM_DESTINATION_TARGET_ID = "dsmBtpDestinationTarget"

    private static final GET_BOOKED_SLOT_URL = "/api/deliveryslotmanagements/getBookedSlot?cartCode={cartCode}"

    @Resource
    private DestinationService<ConsumedDestinationModel> destinationService

    @Resource
    private IntegrationRestTemplateFactory integrationRestTemplateFactory

    @Override
    void populate(OrderModel orderModel, OrderData orderData) throws ConversionException {
        if (orderModel.getDeliveryMode().getCode().startsWith("homedelivery")) {
            ResponseEntity<Map> responseBookedSlot = getBookedSlot(orderModel)
            if (responseBookedSlot.getStatusCode() == HttpStatus.OK) {
                Map deliverySlotMap = responseBookedSlot.getBody()
                orderData.getDeliveryMode().setDescription("You'll receive your items on: " + parseDeliverySlotCode(deliverySlotMap.get("deliverySlot")))
            } else {
                LOG.error("Error getting Booked slot for order {}", orderModel.getCode())
            }
        }
    }

    protected ResponseEntity<Map> getBookedSlot(OrderModel orderModel) {
        return getBtpResponse(HttpMethod.GET, GET_BOOKED_SLOT_URL, ["cartCode": orderModel.getCode()])
    }

    protected String parseDeliverySlotCode(String deliverySlotCode) {
        return deliverySlotCode.substring(4, 15) + ":00"
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
                    btpResponse = restOperations.getForEntity(url, Map.class, uriVariables)
                    break
                default:
                    btpResponse = null
            }
        }
        catch (RestClientResponseException e) {
            btpResponse = new ResponseEntity<>(Collections.emptyMap(), HttpStatus.valueOf(e.getRawStatusCode()))
        }

        return btpResponse
    }

}
