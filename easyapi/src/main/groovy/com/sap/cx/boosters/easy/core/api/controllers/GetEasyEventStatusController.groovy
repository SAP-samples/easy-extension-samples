package com.sap.cx.boosters.easy.core.api.controllers

import com.sap.cx.boosters.easy.core.api.services.EasyAPIService
import com.sap.cx.boosters.easy.core.event.logger.EasyEventLogger
import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.json.JsonOutput

class GetEasyEventStatusController implements EasyRestServiceController {
	
	EasyEventLogger easyEventLogger;
    EasyAPIService easyAPIService;

    Map<String,Object> execute(Map ctx) {

        if (!easyAPIService.isValidAPIKey(ctx))
        {
            return easyAPIService.buildInvalidAPIKeyResponse();
        }

        def response = [:]
		def responseBody = [:]

        try {
				def eventLogs=easyEventLogger.getLogsForEvent(ctx.pathParameters.eventId)
                responseBody = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(eventLogs)
        } catch(Exception e) {
	        response.'responseCode' = 500
	        def errorsMap = [errors:[type: '', reason: '', message: '', errorCode: '']]
	        errorsMap.errors.message = e.getMessage()
	        def jsonErrors = JsonOutput.toJson(errorsMap)
	        response.'body' = jsonErrors
	        return response
        }

        response.'responseCode' = 200;
        response.'body' = responseBody
        return response
    }
}