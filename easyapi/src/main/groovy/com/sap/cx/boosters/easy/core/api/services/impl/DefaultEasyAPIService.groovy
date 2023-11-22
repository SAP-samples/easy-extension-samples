package com.sap.cx.boosters.easy.core.api.services.impl

import com.sap.cx.boosters.easy.core.api.services.EasyAPIService
import groovy.json.JsonOutput

class DefaultEasyAPIService implements EasyAPIService {

    @Override
    boolean isValidAPIKey(Map ctx) {
        String apiKey = ctx.headers['x-api-key']

        return apiKey == "123456"
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
