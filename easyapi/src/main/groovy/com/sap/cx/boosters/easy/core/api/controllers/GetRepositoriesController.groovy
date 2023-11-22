package com.sap.cx.boosters.easy.core.api.controllers

import com.sap.cx.boosters.easy.core.api.services.EasyAPIService
import com.sap.cx.boosters.easy.core.data.EasyExtension
import com.sap.cx.boosters.easy.core.data.EasyRepository
import com.sap.cx.boosters.easy.core.repository.service.EasyRepositoryService
import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController
import groovy.json.JsonOutput
import org.slf4j.LoggerFactory
import javax.annotation.Resource

class GetRepositoriesController implements EasyRestServiceController {
	
	@Resource
	EasyRepositoryService easyRepositoryService;

	@Resource
	EasyAPIService easyAPIService;

	private static final LOG = LoggerFactory.getLogger(GetRepositoriesController.class);

	@Override
    Map<String,Object> execute(Map ctx) {

		if (!easyAPIService.isValidAPIKey(ctx))
		{
			return easyAPIService.buildInvalidAPIKeyResponse();
		}

        def response = [:]
		def repositories = []

        try {
			List<EasyRepository> easyRepositories = easyRepositoryService.getRepositories()
			easyRepositories.each{ 
				def repository = [code:it.code, name:it.name];
				repositories.add(repository);
			}
        } catch(Exception e) {
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
        response.'body' = JsonOutput.toJson(repositories);
        return response;
    }
}