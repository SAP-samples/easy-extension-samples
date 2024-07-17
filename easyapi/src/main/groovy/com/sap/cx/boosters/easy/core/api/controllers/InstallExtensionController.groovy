package com.sap.cx.boosters.easy.core.api.controllers

class InstallExtensionController extends AbstractEasyEventController {

    @Override
    protected String getEventId(Map<String, Object> eventData) {
        return easyInstaller.install(eventData.repository as String, eventData.extensionId as String, eventData.configuration as Map<String, Object>, eventData.async as boolean)
    }

    @Override
    protected String getEventType() {
        return "Installation"
    }
}
