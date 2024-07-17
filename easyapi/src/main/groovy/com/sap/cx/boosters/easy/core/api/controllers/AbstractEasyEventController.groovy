package com.sap.cx.boosters.easy.core.api.controllers

import com.sap.cx.boosters.easy.core.installer.EasyInstaller

import javax.annotation.Resource

abstract class AbstractEasyEventController extends AbstractEasyApiController {
    @Resource
    protected EasyInstaller easyInstaller

    @Override
    def executeInternal(def ctx) {
        def eventData = [:] as Map<String, Object>
        eventData.repository = ctx.pathParameters.repositoryCode?.toString()
        eventData.extensionId = ctx.pathParameters.extensionCode?.toString()
        eventData.configuration = ctx.body
        eventData.async = ctx.parameters.async as Boolean

        def responseBody = [:]

        responseBody.'eventId' = getEventId(eventData)

        def messageFragment
        if (eventData.extensionId == null) {
            messageFragment = "repository '$eventData.repository'"
        } else {
            messageFragment = "repository '$eventData.repository' and extension '$eventData.extensionId'"
        }
        responseBody.'message' = "${getEventType()} request for $messageFragment submitted."
        return responseBody
    }

    protected abstract String getEventId(Map<String, Object> eventData)

    protected abstract String getEventType()
}
