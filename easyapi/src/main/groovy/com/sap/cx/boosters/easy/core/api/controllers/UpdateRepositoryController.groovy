package com.sap.cx.boosters.easy.core.api.controllers

import com.sap.cx.boosters.easy.core.api.services.EasyAPIService
import com.sap.cx.boosters.easy.core.installer.EasyInstaller
import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController;
import groovy.json.JsonOutput;

class UpdateRepositoryController implements EasyRestServiceController {

	EasyInstaller easyInstaller;
	EasyAPIService easyAPIService;
	
	Map<String,Object> execute(Map ctx) {

		if (!easyAPIService.isValidAPIKey(ctx))
		{
			return easyAPIService.buildInvalidAPIKeyResponse();
		}

		 def response = [:];
		 def responseBody = [:];
		 try {
			 responseBody.'eventId' = easyInstaller.updateRepository(ctx.pathParameters.repositoryCode, ctx.parameters.async.toBoolean());
		 } catch(Exception e)
		 {
			 response.'responseCode' = 500;
			 def errorsMap = [errors:[type: '', reason: '', message: '', errorCode: '']]
			 errorsMap.errors.message = e.getMessage();
			 def jsonErrors = JsonOutput.toJson(errorsMap);
			 response.'body' = jsonErrors;
			 return response
		}
		
		response.'responseCode' = 200;
		
		responseBody.'message' = 'Update request for repository ' + ctx.pathParameters.repositoryCode + ' submitted'
		response.'body' = JsonOutput.toJson(responseBody);
		return response;
    }
}