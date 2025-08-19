package com.sap.cx.boosters.easy.deliverybtp.controller

import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.apiregistryservices.services.DestinationService
import de.hybris.platform.commerceservices.order.dao.impl.DefaultCommerceOrderDao
import de.hybris.platform.core.model.order.CartModel
import de.hybris.platform.core.model.order.OrderModel
import de.hybris.platform.outboundservices.client.IntegrationRestTemplateFactory
import de.hybris.platform.servicelayer.internal.dao.DefaultGenericDao
import groovy.json.JsonBuilder
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestClientResponseException

import jakarta.annotation.Resource

abstract class AbstractBtpController implements EasyRestServiceController {

    private static final def LOG = LoggerFactory.getLogger(AbstractBtpController)

    private static final DSM_DESTINATION_ID = "dsmBtpDestination"

    private static final DSM_DESTINATION_TARGET_ID = "dsmBtpDestinationTarget"

    @Resource
    private DefaultGenericDao<CartModel> defaultCartGenericDao

    @Resource
    private DestinationService<ConsumedDestinationModel> destinationService

    @Resource
    private IntegrationRestTemplateFactory integrationRestTemplateFactory

    @Resource
    private DefaultCommerceOrderDao commerceOrderDao

    protected CartModel getCart(String userId, String cartId) {
        CartModel cart
        if (userId.equalsIgnoreCase("anonymous")) {
            def cartList = defaultCartGenericDao.find([guid: cartId])
            cart = (cartList && !cartList.isEmpty()) ? cartList.get(0) : null
        } else {
            def cartList = defaultCartGenericDao.find([code: cartId])
            cart = (cartList && !cartList.isEmpty()) ? cartList.get(0) : null
        }
        if (cart != null)
            return cart
        else
            throw new IllegalArgumentException("Cart is not valid")
    }

    protected OrderModel getOrder(String orderId) {
        def orderList = commerceOrderDao.find([code: orderId])
        def order = (orderList && !orderList.isEmpty()) ? orderList.get(0) : null
        if (order != null)
            return order
        else
            throw new IllegalArgumentException("Order is not valid")
    }


    protected getBtpResponse(HttpMethod method, String restUrl, Map uriVariables) {
        def response = [:]
        def destination = destinationService.getDestinationByIdAndByDestinationTargetId(DSM_DESTINATION_ID, DSM_DESTINATION_TARGET_ID)
        if (destination == null) {
            def errorMessage = "Destination not found for destination Id:$DSM_DESTINATION_ID and destination Target Id:$DSM_DESTINATION_TARGET_ID"
            LOG.error(errorMessage)
            return handleError(errorMessage)
        }

        def restOperations = integrationRestTemplateFactory.create(destination)
        def url = "$destination.url$restUrl"

        try {
            def btpResponse
            switch (method) {
                case HttpMethod.GET:
                    btpResponse = restOperations.getForEntity(url, Object.class, uriVariables)
                    break
                case HttpMethod.POST:
                    btpResponse = restOperations.postForEntity(url, null, Object.class, uriVariables)
                    break
                case HttpMethod.PUT:
                    btpResponse = restOperations.exchange(url, HttpMethod.PUT, null, Object.class, uriVariables)
                    break
                case HttpMethod.DELETE:
                    btpResponse = restOperations.exchange(url, HttpMethod.DELETE, null, Object.class, uriVariables)
                    break
                default:
                    btpResponse = null
            }
            response.'responseCode' = btpResponse.getStatusCodeValue()
            response.'body' = new JsonBuilder(btpResponse.getBody()).toPrettyString()
        } catch (RestClientResponseException e) {
            response.'responseCode' = e.getRawStatusCode()
            response.'body' = e.getResponseBodyAsString()
        }
        return response
    }


    private static Map<String, Object> handleError(String errorMessage) {
        def response = [:] as Map<String, Object>
        response.'responseCode' = 500
        response.'body' = errorMessage
        return response
    }

}
