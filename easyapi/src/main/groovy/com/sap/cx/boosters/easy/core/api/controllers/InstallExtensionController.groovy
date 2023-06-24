package com.sap.cx.boosters.easy.core.api.controllers

import com.sap.cx.boosters.easy.core.api.services.EasyAPIService
import com.sap.cx.boosters.easy.core.installer.EasyInstaller
import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController;
import groovy.json.JsonOutput
import org.slf4j.LoggerFactory;

class InstallExtensionController implements EasyRestServiceController {

	EasyInstaller easyInstaller;
	EasyAPIService easyAPIService;

	private static final LOG = LoggerFactory.getLogger(GetRepositoriesController.class);
	
    Map<String,Object> execute(Map ctx) {

		if (!easyAPIService.isValidAPIKey(ctx))
		{
			return easyAPIService.buildInvalidAPIKeyResponse();
		}

		def response = [:];
		def responseBody = [:];
		try {
			responseBody.'eventId' = easyInstaller.install(ctx.pathParameters.repositoryCode, ctx.pathParameters.extensionCode, ctx.parameters.async.toBoolean());
		} catch(Exception e) 
		{
			LOG.error("Unexpected error", e)
			response.'responseCode' = 500;
			def errorsMap = [errors: [[type: '', message: '']]]
			errorsMap.errors[0].type = "SystemError"
			errorsMap.errors[0].message = e.getMessage()
			def jsonErrors = JsonOutput.toJson(errorsMap);
			response.'body' = jsonErrors;
			return response
		}
			
		response.'responseCode' = 200;
		
		responseBody.'message' = 'Installation request for extension ' + ctx.pathParameters.extensionCode + ' submitted.'
		response.'body' = JsonOutput.toJson(responseBody);
			
		return response;
    }
}