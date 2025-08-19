package com.sap.cx.boosters.easy.core.api.controllers

import com.sap.cx.boosters.easy.core.api.data.EasyInstallerEventResponse
import com.sap.cx.boosters.easy.core.installer.EasyInstaller

import jakarta.annotation.Resource

abstract class AbstractEasyEventController extends AbstractEasyApiController {
    @Resource
    protected EasyInstaller easyInstaller

    @Override
    def executeInternal(def ctx) {
        def eventData = [:] as Map<String, Object>
        eventData.repository = ctx.pathParameters.repositoryCode?.toString()
        eventData.extensionId = ctx.pathParameters.extensionCode?.toString()
        eventData.configuration = ctx.body
        if (ctx.parameters.async) {
            eventData.async = Boolean.valueOf(ctx.parameters.async as String)
        }

        def responseBody = new EasyInstallerEventResponse()

        responseBody.eventId = getEventId(eventData)

        def messageFragment
        if (eventData.extensionId == null) {
            messageFragment = "repository '$eventData.repository'"
        } else {
            messageFragment = "repository '$eventData.repository' and extension '$eventData.extensionId'"
        }
        responseBody.message = "${getEventType()} request for $messageFragment ${eventData.async ? 'submitted' : 'processed'}."
        return responseBody
    }

    protected abstract String getEventId(Map<String, Object> eventData)

    protected abstract String getEventType()
}
