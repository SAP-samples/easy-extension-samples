package com.sap.cx.boosters.easy.core.api.controllers

import com.sap.cx.boosters.easy.core.helper.EasyCoreHelper
import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController
import com.sap.cx.boosters.easyrest.enums.AuthenticationMethod
import com.sap.cx.boosters.easyrest.service.EasyRestService
import com.sap.cx.boosters.easyrest.enums.EndpointType
import groovy.json.JsonOutput
import groovy.json.JsonSlurper

import jakarta.annotation.Resource

class SwaggerController implements EasyRestServiceController {

    @Resource
    EasyRestService easyRestService

    @Resource
    EasyCoreHelper easyCoreHelper

    @Override
    Map<String, Object> execute(Map ctx) {
        JsonSlurper slurper = new JsonSlurper()

        String baseUrl = resolveBaseUrl(ctx?.headers?.host)
        String tokenUrl = baseUrl + '/authorizationserver/oauth/token'
        String authorizationUrl = baseUrl + '/authorizationserver/oauth/authorize'

        Map<String, Object> openAPI = [
                openapi   : '3.0.1',
                servers   : [
                        [
                                url: "${baseUrl}/easyrest",
                                description: 'Generated server url'
                        ]
                ],
                info      : [
                        title  : 'Easy REST API',
                        description: 'This API allows you to manage all of the Easy Rest APIs',
                        license: [
                                name: 'Licensing information',
                                url : 'https://sap.github.io/easy-extension-framework/latest/#licensing'
                        ],
                        version: easyCoreHelper.getDeployedEasyVersion()
                ],
                externalDocs: [
                        description: 'Easy Extension Framework Documentation',
                        url        : "https://sap.github.io/easy-extension-framework/"
                ],
                security  : [
                        [oauth2_authorization_code: ['basic']],
                        [oauth2_client_credentials: ['extended']]
                ],
                paths     : [:],
                components: [
                        securitySchemes: [
                                oauth2_client_credentials: [
                                        type : 'oauth2',
                                        description: 'OAuth2 Client Credentials flow',
                                        flows: [
                                                clientCredentials: [
                                                        tokenUrl  : "$tokenUrl",
                                                        scopes    : [
                                                                extended: ''
                                                        ]
                                                ]
                                        ]
                                ],
                                oauth2_authorization_code: [
                                        type : 'oauth2',
                                        description: 'OAuth2 Authorization Code flow',
                                        flows: [
                                                authorizationCode: [
                                                        authorizationUrl: "$authorizationUrl",
                                                        tokenUrl        : "$tokenUrl",
                                                        scopes          : [
                                                                basic       : ''
                                                        ]
                                                ]
                                        ]
                                ],
                                api_key_auth            : [
                                        type: 'apiKey',
                                        description: 'API Key Authentication',
                                        in  : 'header',
                                        name: 'x-api-key'
                                ],
                                jwt_auth         : [
                                        type: 'http',
                                        description: 'JWT Authentication',
                                        scheme: 'bearer',
                                        bearerFormat: 'JWT'
                                ]
                        ]
                ],
                'x-sap-api-type': 'Rest',
                'x-sap-shortText': 'Enables you to manage all of the Easy Rest endpoints.',
                'x-sap-stateInfo': [
                        state: 'Active'
                ],
                'x-servers': [
                        [
                                templates: [
                                        url: [
                                                description: 'SAP Commerce Cloud server URL where the application is deployed.'
                                        ]
                                ],
                                description: 'Easy Rest API base url',
                                url: 'https://{url}/easyrest',
                        ]
                ]
        ] as Map<String, Object>

        def easyRests = easyRestService.getAllEasyRests(EndpointType.EASY_REST)
        def tags = [] as Set<String>

        easyRests.each {
            String method = it.method.code.toLowerCase()
            String pathKey = '/' + it.path
            String group = it.easyRestGroup?.name ?: 'Default'
            tags.add(group)

            def operation = [
                    operationId: buildOperationId(method, it.path),
                    summary    : it.swaggerSummary ?: '',
                    description: it.swaggerDescription ?: '',
                    security   : buildSecurityRequirements(it.authenticationMethod),
                    parameters : parseJsonIfPresent(it.swaggerParameters, slurper) ?: [],
                    responses  : parseJsonIfPresent(it.swaggerResponses, slurper) ?: [
                            '200': [description: 'Successful response']
                    ],
                    tags       : [group]
            ]

            if (it.formatText != null && it.formatText != '') {
                def requestBody = parseJsonIfPresent(it.swaggerRequestBody, slurper)
                if (requestBody != null) {
                    operation.requestBody = requestBody
                }
            }

            def pathItem = openAPI.paths[pathKey] ?: [:]
            pathItem[method] = operation
            openAPI.paths[pathKey] = pathItem
        }

        openAPI.tags = tags.collect { [name: it] }.sort { it.name }

        def jsonSwagger = JsonOutput.toJson(openAPI)
        return [
                description: '',
                body       : jsonSwagger
        ]
    }

    private static String resolveBaseUrl(String hostHeader) {
        if (!hostHeader) {
            return ''
        }

        if (hostHeader.startsWith('http://') || hostHeader.startsWith('https://')) {
            return hostHeader
        }

        return 'https://' + hostHeader
    }

    private static String buildOperationId(String method, String path) {
        String normalizedPath = (path ?: 'unknown').replaceAll('[^a-zA-Z0-9_]', '_')
        return method + '_' + normalizedPath
    }

    private static Object parseJsonIfPresent(String payload, JsonSlurper slurper) {
        if (!payload?.trim()) {
            return null
        }
        return slurper.parseText(payload)
    }

    private static List<Map<String, Object>> buildSecurityRequirements(AuthenticationMethod authenticationMethod) {
        if (authenticationMethod == AuthenticationMethod.OAUTH) {
            return [
                    [oauth2_authorization_code: ['basic']],
                    [oauth2_client_credentials: ['extended']]
            ]
        }

        if (authenticationMethod == AuthenticationMethod.API_KEY) {
            return [[APIKeyAuth: []]]
        }

        if (authenticationMethod == AuthenticationMethod.JWT) {
            return [[JavaWebTokens: []]]
        }

        return []
    }
}
