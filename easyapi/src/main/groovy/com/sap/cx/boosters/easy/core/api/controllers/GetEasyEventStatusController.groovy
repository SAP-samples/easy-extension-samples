package com.sap.cx.boosters.easy.core.api.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.sap.cx.boosters.easy.core.event.logger.EasyLogger

import jakarta.annotation.Resource

class GetEasyEventStatusController extends AbstractEasyApiController {

    @Resource
    private EasyLogger easyLogger

    @Override
    def executeInternal(def ctx) {
        def eventLogs = easyLogger.getLogsForEvent("${ctx.pathParameters.eventId}")
        return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(eventLogs)
    }
}
