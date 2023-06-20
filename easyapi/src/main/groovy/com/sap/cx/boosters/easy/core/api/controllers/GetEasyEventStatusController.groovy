package com.sap.cx.boosters.easy.core.api.controllers

import com.sap.cx.boosters.easy.core.event.logger.EasyEventLogger
import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController
import com.sap.cx.boosters.easy.core.event.log.EasyEventLogData

import com.fasterxml.jackson.databind.ObjectMapper
import groovy.json.JsonOutput

class GetEasyEventStatusController implements EasyRestServiceController {
	
	EasyEventLogger easyEventLogger;

    Map<String,Object> execute(Map ctx) {
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