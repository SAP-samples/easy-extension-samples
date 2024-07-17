package com.sap.cx.boosters.easy.core.api.controllers

class UpdateRepositoryController extends AbstractEasyEventController {

    @Override
    protected String getEventId(Map<String, Object> eventData) {
        return easyInstaller.updateRepository(eventData.repository as String, eventData.async as boolean)
    }

    @Override
    protected String getEventType() {
        return "Update repository"
    }
}
