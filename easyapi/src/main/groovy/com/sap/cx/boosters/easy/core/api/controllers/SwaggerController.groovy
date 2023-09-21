package com.sap.cx.boosters.easy.core.api.controllers


import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController
import com.sap.cx.boosters.easyrest.service.EasyRestService
import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.slf4j.LoggerFactory
import org.slf4j.Logger
import de.hybris.platform.servicelayer.config.ConfigurationService

class SwaggerController implements EasyRestServiceController {

	EasyRestService easyRestService;

	ConfigurationService configurationService;
	
    Map<String,Object> execute(Map ctx) {
        def response = [:]		
		
		Logger log = LoggerFactory.getLogger('easyrest');
		JsonSlurper slurper = new groovy.json.JsonSlurper();
		
		def openAPI = [
		   openapi: '3.0.3',
		   servers: [
			  [
				  url: '/easyrest'
			  ]
		   ],
		   info: [
			  title: 'Easy REST API',
			  version: configurationService.getConfiguration().getString('easy.version', '0.2.1')
		   ],
		   paths: [:],
		   components: [
			  securitySchemes: [
				 oauth2_password: [
					type: 'oauth2',
					flows: [
					   password: [
						  tokenUrl: 'https://localhost:9002/authorizationserver/oauth/token'
					   ],
					]
				 ],
				 oauth2_clientCredentials: [
					type: 'oauth2',
					flows: [
					   clientCredentials: [
						  tokenUrl: 'https://localhost:9002/authorizationserver/oauth/token'
					   ]
					]
				 ],
				 ApiKeyAuth: [
					 type: 'apiKey',
					 in: 'header',
					 name: 'X-API-KEY'
				 ]
			  ]
		   ]
		]
		
		def easyRests = easyRestService.getAllEasyRests();
		
		easyRests.each {
		
		   def path = it.path;
		   String method=it.method.code.toLowerCase();
		   String group = it.easyRestGroup ? it.easyRestGroup.name : "Default";
		   path = [
				  (method) : [
					  'operationId' : "$path",
					  'summary': '',
					  'description': '',
					  'security': [],
					  'parameters': [],
					  'requestBody': [],
					  'responses': [:],
					  'tags': [group]
				  ]
			  ];
		
		   path[method].summary = it.swaggerSummary;
		   path[method].description = it.swaggerDescription;
		
		   if (it.authenticationMethod == com.sap.cx.boosters.easyrest.enums.AuthenticationMethod.OAUTH)
			  path[method].security.add([oauth2_password: [], oauth2_clientCredentials: []]);
			  
		   if (it.secret != null && it.secret != '')
			  path[method].security.add(ApiKeyAuth: []);
		
		   def swaggerParameters = it.swaggerParameters;
		   if (swaggerParameters != null && swaggerParameters != '')
		   {
			  def jsonParameters = slurper.parseText(swaggerParameters);
			  path[method].parameters = jsonParameters;
		   }
		
		  if (it.formatText != null && it.formatText != '')
		  {
			  def swaggerRequestBody = it.swaggerRequestBody;
			  if (swaggerRequestBody != null && swaggerRequestBody != '')
			  {
				 def jsonRequestBody = slurper.parseText(swaggerRequestBody);
				 path[method].requestBody = jsonRequestBody;
			  }
		   }
		
		   response = [
			  'description': ''
		   ]
		
		   def swaggerResponses = it.swaggerResponses;
		   if (swaggerResponses != null && swaggerResponses != '')
		   {
			  def jsonResponses = slurper.parseText(swaggerResponses);
			  path[method].responses = jsonResponses;
		   }
		
		   openAPI.paths.put('/' + it.path, path);
		}
		
		def jsonSwagger = groovy.json.JsonOutput.toJson(openAPI);
		response.body = jsonSwagger;
		
        return response;
    }
}
