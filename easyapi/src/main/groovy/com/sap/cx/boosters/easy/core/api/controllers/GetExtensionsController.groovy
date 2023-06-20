package com.sap.cx.boosters.easy.core.api.controllers

import com.sap.cx.boosters.easy.core.data.EasyRepository
import com.sap.cx.boosters.easy.core.repository.service.EasyRepositoryService
import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController
import groovy.json.JsonOutput
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
import com.fasterxml.jackson.databind.ObjectMapper

class GetExtensionsController implements EasyRestServiceController {
	
	EasyRepositoryService easyRepositoryService;

    Map<String,Object> execute(Map ctx) {
		
		if (!isValidApiKey(ctx))
		{
			def response = [:]
			response.'responseCode' = 403
			def errorsMap = [errors:[type: '', reason: '', message: '', errorCode: '']]
			errorsMap.errors.message = "Invalid API Key"
			def jsonErrors = JsonOutput.toJson(errorsMap)
			response.'body' = jsonErrors
			return response
		}
		
        def response = [:]
		def responseBody = [:]

        try {
	        EasyRepository repository = easyRepositoryService.getRepositoryForCode(ctx.pathParameters.repositoryCode)
	        if (repository == null) {
	        	throw new UnknownIdentifierException("No repository found with code: " + ctx.pathParameters.repositoryCode)
	        } else {
			    responseBody=new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(repository.getExtensions())
			}
        } catch(Exception e) {
	        response.'responseCode' = 500
	        def errorsMap = [errors:[type: '', reason: '', message: '', errorCode: '']]
	        errorsMap.errors.message = e.getMessage()
	        def jsonErrors = JsonOutput.toJson(errorsMap)
	        response.'body' = jsonErrors
	        return response
        }

        response.'responseCode' = 200
        response.'body' = responseBody
        return response
    }
	
	
	boolean isValidApiKey(Map ctx) {		
		String apiKey = ctx.headers.'x-api-key'

		if (apiKey.equals("123456"))
			return true
		else
			return false
	}
	
}