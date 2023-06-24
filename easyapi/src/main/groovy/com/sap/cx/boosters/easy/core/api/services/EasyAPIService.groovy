package com.sap.cx.boosters.easy.core.api.services

import groovy.json.JsonOutput

class EasyAPIService {

    boolean isValidAPIKey(Map ctx) {
        String apiKey = ctx.headers.'x-api-key'

        if (apiKey.equals("123456"))
            return true
        else
            return false
    }

    Map<String,Object> buildInvalidAPIKeyResponse() {
        def response = [:]
        response.'responseCode' = 403
        def errorsMap = [errors: [[type: '', message: '']]]
        errorsMap.errors[0].type = "AccessError"
        errorsMap.errors[0].message = "Invalid API Key"
        def jsonErrors = JsonOutput.toJson(errorsMap)
        response.'body' = jsonErrors
        return response
    }
}