package com.sap.cx.boosters.easy.core.api.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.sap.cx.boosters.easy.core.data.EasyExtension
import com.sap.cx.boosters.easy.core.data.EasyRepository
import com.sap.cx.boosters.easy.core.extension.EasyExtensionService
import com.sap.cx.boosters.easy.core.repository.service.EasyRepositoryService
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException

import jakarta.annotation.Resource

class GetExtensionDetailsController extends AbstractEasyApiController {

    @Resource
    EasyExtensionService easyExtensionService

    @Override
    def executeInternal(def ctx) {
        EasyExtension extension = easyExtensionService.getExtensionByRepositoryCodeAndExtensionId("${ctx.pathParameters.repositoryCode}", "${ctx.pathParameters.extensionId}")
        if (extension == null) {
            throw new UnknownIdentifierException("No extension found for id ['${ctx.pathParameters.extensionId}'] in repository ['${ctx.pathParameters.repositoryCode}']")
        } else {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(extension)
        }
    }

}
