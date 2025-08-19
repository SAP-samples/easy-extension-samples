package com.sap.cx.boosters.easy.core.api.controllers

import com.sap.cx.boosters.easy.core.api.services.EasyAPIService
import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController
import groovy.json.JsonOutput
import org.slf4j.LoggerFactory

import jakarta.annotation.Resource

abstract class AbstractEasyApiController implements EasyRestServiceController {
    private static final def LOG = LoggerFactory.getLogger(getClass())

    @Resource
    private EasyAPIService easyAPIService

    @Override
    Map<String, Object> execute(Map ctx) {
            def response = [:] as Map<String, Object>
            try {
                response.'body' = executeInternal(ctx)
                response.'responseCode' = 200
                return response
            } catch (Exception exception) {
                LOG.error("Unexpected error", exception)
                response.'responseCode' = 500
                def errorsMap = [errors: [[type: '', message: '']]]
                errorsMap.errors[0].type = "SystemError"
                errorsMap.errors[0].message = exception.getMessage()
                def jsonErrors = JsonOutput.toJson(errorsMap)
                response.'body' = jsonErrors
                return response
            }
    }

    abstract def executeInternal(def ctx)

}
