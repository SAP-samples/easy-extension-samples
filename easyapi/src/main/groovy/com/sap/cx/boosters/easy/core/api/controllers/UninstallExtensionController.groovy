package com.sap.cx.boosters.easy.core.api.controllers

class UninstallExtensionController extends AbstractEasyEventController {

    @Override
    protected String getEventId(Map<String, Object> eventData) {
        return easyInstaller.uninstall(eventData.repository as String, eventData.extensionId as String, eventData.async as boolean)
    }

    @Override
    protected String getEventType() {
        return "Uninstallation"
    }
}
