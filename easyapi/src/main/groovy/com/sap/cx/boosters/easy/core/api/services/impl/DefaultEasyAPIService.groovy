package com.sap.cx.boosters.easy.core.api.services.impl

import com.sap.cx.boosters.easy.core.api.services.EasyAPIService
import de.hybris.platform.servicelayer.search.FlexibleSearchService
import groovy.json.JsonOutput

import javax.annotation.Resource

class DefaultEasyAPIService implements EasyAPIService {

    public static final QUERY_GET_API_KEY = 'select {pk} from {EasyApiKey as eak} where {eak.key} = ?key'

    @Resource
    private FlexibleSearchService flexibleSearchService

    @Override
    boolean isValidAPIKey(Map ctx) {
        String apiKey = ctx.headers['x-api-key']
        return flexibleSearchService.search(QUERY_GET_API_KEY, [key: apiKey]).getCount() > 0
    }

    @Override
    Map<String, Object> buildInvalidAPIKeyResponse() {
        def response = [:] as Map<String, Object>
        response.'responseCode' = 403
        def errorsMap = [errors: [[type: '', message: '']]]
        errorsMap.errors[0].type = "AccessError"
        errorsMap.errors[0].message = "Invalid API Key"
        def jsonErrors = JsonOutput.toJson(errorsMap)
        response.'body' = jsonErrors
        return response
    }
}
