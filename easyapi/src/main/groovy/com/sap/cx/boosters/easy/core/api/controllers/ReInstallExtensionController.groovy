package com.sap.cx.boosters.easy.core.api.controllers

class ReInstallExtensionController extends AbstractEasyEventController {

    @Override
    protected String getEventId(Map<String, Object> eventData) {
        return easyInstaller.reinstall(eventData.repository as String, eventData.extensionId as String, eventData.async as boolean)
    }

    @Override
    protected String getEventType() {
        return "Re-installation"
    }
}
