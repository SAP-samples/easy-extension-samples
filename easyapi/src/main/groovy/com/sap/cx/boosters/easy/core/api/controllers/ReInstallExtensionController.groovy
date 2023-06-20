package com.sap.cx.boosters.easy.core.api.controllers

import com.sap.cx.boosters.easy.core.installer.EasyInstaller
import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController;
import groovy.json.JsonOutput;

class ReInstallExtensionController implements EasyRestServiceController {

	EasyInstaller easyInstaller;
	
    Map<String,Object> execute(Map ctx) {
		def response = [:];
		def responseBody = [:];
		try {
			responseBody.'eventId' = easyInstaller.reinstall(ctx.pathParameters.repositoryCode, ctx.pathParameters.extensionCode, ctx.parameters.async.toBoolean());
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
		
		responseBody.'message' = 'Reinstallation request for extension ' + ctx.pathParameters.extensionCode + ' submitted.'
		response.'body' = JsonOutput.toJson(responseBody);
			
		return response;
    }
}