package com.sap.cx.boosters.easy.core.api.controllers

class InstallExtensionController extends AbstractEasyEventController {

    @Override
    protected String getEventId(String repositoryCode, String extensionId, Boolean async) {
        return easyInstaller.install(repositoryCode, extensionId, async)
    }

    @Override
    protected String getEventType() {
        return "Installation"
    }
}
