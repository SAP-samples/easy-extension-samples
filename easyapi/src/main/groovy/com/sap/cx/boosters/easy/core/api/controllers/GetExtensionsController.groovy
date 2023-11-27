package com.sap.cx.boosters.easy.core.api.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.sap.cx.boosters.easy.core.data.EasyRepository
import com.sap.cx.boosters.easy.core.repository.service.EasyRepositoryService
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException

import javax.annotation.Resource

class GetExtensionsController extends AbstractEasyApiController {

    @Resource
    EasyRepositoryService easyRepositoryService

    @Override
    def executeInternal(def ctx) {

        EasyRepository repository = easyRepositoryService.getRepositoryForCode("${ctx.pathParameters.repositoryCode}")
        if (repository == null) {
            throw new UnknownIdentifierException("No repository found with code: ${ctx.pathParameters.repositoryCode}")
        } else {
            return new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(repository.getExtensions())
        }
    }

}
