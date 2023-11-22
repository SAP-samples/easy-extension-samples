package com.sap.cx.boosters.easy.core.api.controllers

class ReInstallExtensionController extends AbstractEasyEventController {

    @Override
    protected String getEventId(String repositoryCode, String extensionId, Boolean async) {
        return easyInstaller.reinstall(repositoryCode, extensionId, async)
    }

    @Override
    protected String getEventType() {
        return "Re-installation"
    }
}
