package com.sap.cx.boosters.easy.core.api.controllers

import com.sap.cx.boosters.easy.core.helper.EasyCoreHelper
import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController
import com.sap.cx.boosters.easyrest.enums.AuthenticationMethod
import com.sap.cx.boosters.easyrest.service.EasyRestService
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
                openapi   : '3.0.3',
                servers   : [
                        [
                                url: '/easyrest'
                        ]
                ],
                info      : [
                        title  : 'Easy REST API',
                        version: easyCoreHelper.getDeployedEasyVersion()
                ],
                paths     : [:],
                components: [
                        securitySchemes: [
                                oauth2_authorizationCode: [
                                        type : 'oauth2',
                                        description: 'OAuth2 Authorization Code flow',
                                        flows: [
                                                authorizationCode: [
                                                        authorizationUrl: "$authorizationUrl",
                                                        tokenUrl        : "$tokenUrl",
                                                        refreshUrl      : "$tokenUrl",
                                                        scopes          : [
                                                                'openid'        : 'OpenID Connect scope',
                                                                'profile'       : 'Profile information',
                                                                'email'         : 'Email address',
                                                                'offline_access': 'Refresh token support'
                                                        ]
                                                ]
                                        ]
                                ],
                                oauth2_clientCredentials: [
                                        type : 'oauth2',
                                        description: 'OAuth2 Client Credentials flow',
                                        flows: [
                                                clientCredentials: [
                                                        tokenUrl  : "$tokenUrl",
                                                        refreshUrl: "$tokenUrl",
                                                        scopes    : [
                                                                'easyrest.read' : 'Read access to Easy REST endpoints',
                                                                'easyrest.write': 'Write access to Easy REST endpoints'
                                                        ]
                                                ]
                                        ]
                                ],
                                "APIKeyAuth"            : [
                                        type: 'apiKey',
                                        description: 'API Key Authentication',
                                        in  : 'header',
                                        name: 'x-api-key'
                                ],
                                "JavaWebTokens"         : [
                                        type: 'http',
                                        description: 'JWT Authentication',
                                        scheme: 'bearer',
                                        bearerFormat: 'JWT'
                                ]
                        ]
                ]
        ] as Map<String, Object>

        def easyRests = easyRestService.getAllEasyRests()
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
                    [oauth2_authorizationCode: ['openid', 'profile', 'email']],
                    [oauth2_clientCredentials: ['easyrest.read']]
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
