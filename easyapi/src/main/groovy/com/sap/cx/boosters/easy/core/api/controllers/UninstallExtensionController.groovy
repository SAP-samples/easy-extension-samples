package com.sap.cx.boosters.easy.core.api.controllers

class UninstallExtensionController extends AbstractEasyEventController {

    @Override
    protected String getEventId(String repositoryCode, String extensionId, Boolean async) {
        return easyInstaller.uninstall(repositoryCode, extensionId, async)
    }

    @Override
    protected String getEventType() {
        return "Uninstallation"
    }
}
