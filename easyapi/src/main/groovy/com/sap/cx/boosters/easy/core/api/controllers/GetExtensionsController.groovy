package com.sap.cx.boosters.easy.core.api.controllers

import com.sap.cx.boosters.easy.core.data.EasyRepository
import com.sap.cx.boosters.easy.core.repository.service.EasyRepositoryService
import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController
import groovy.json.JsonOutput
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
import com.fasterxml.jackson.databind.ObjectMapper
import com.sap.cx.boosters.easy.core.api.services.EasyAPIService
import org.slf4j.LoggerFactory
import javax.annotation.Resource


class GetExtensionsController implements EasyRestServiceController {
	
	@Resource
	EasyRepositoryService easyRepositoryService

	@Resource
	EasyAPIService easyAPIService

	private static final LOG = LoggerFactory.getLogger(GetExtensionsController.class);

	@Override
    Map<String,Object> execute(Map ctx) {
		
		if (!easyAPIService.isValidAPIKey(ctx))
		{
			return easyAPIService.buildInvalidAPIKeyResponse();
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
			LOG.error("Unexpected error", e)
	        response.'responseCode' = 500
			def errorsMap = [errors: [[type: '', message: '']]]
			errorsMap.errors[0].type = "SystemError"
			errorsMap.errors[0].message = e.getMessage()
	        def jsonErrors = JsonOutput.toJson(errorsMap)
	        response.'body' = jsonErrors
	        return response
        }

        response.'responseCode' = 200
        response.'body' = responseBody
        return response
    }
	
}