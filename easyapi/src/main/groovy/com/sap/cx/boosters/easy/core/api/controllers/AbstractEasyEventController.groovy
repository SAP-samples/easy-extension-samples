package com.sap.cx.boosters.easy.core.api.controllers

import com.sap.cx.boosters.easy.core.installer.EasyInstaller

import javax.annotation.Resource

abstract class AbstractEasyEventController extends AbstractEasyApiController {
    @Resource
    protected EasyInstaller easyInstaller

    @Override
    def executeInternal(def ctx) {

        def repositoryCode = ctx.pathParameters.repositoryCode?.toString()
        def extensionId = ctx.pathParameters.extensionCode?.toString()
        def async = ctx.parameters.async as Boolean

        def responseBody = [:]
        responseBody.'eventId' = getEventId(repositoryCode, extensionId, async)

        def messageFragment
        if (extensionId == null) {
            messageFragment = "repository '$repositoryCode'"
        } else {
            messageFragment = "repository '$repositoryCode' and extension '$extensionId'"
        }
        responseBody.'message' = "${getEventType()} request for $messageFragment submitted."
        return responseBody
    }

    protected abstract String getEventId(String repositoryCode, String extensionId, Boolean async)

    protected abstract String getEventType()
}
