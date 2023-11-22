package com.sap.cx.boosters.easy.core.api.controllers

class UpdateRepositoryController extends AbstractEasyEventController {

    @Override
    protected String getEventId(String repositoryCode, String extensionId, Boolean async) {
        return easyInstaller.updateRepository(repositoryCode, async)
    }

    @Override
    protected String getEventType() {
        return "Update repository"
    }
}
