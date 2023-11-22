package com.sap.cx.boosters.easy.core.api.controllers


import com.sap.cx.boosters.easy.core.data.EasyRepository
import com.sap.cx.boosters.easy.core.repository.service.EasyRepositoryService
import groovy.json.JsonOutput

import javax.annotation.Resource

class GetRepositoriesController extends AbstractEasyApiController {

    @Resource
    EasyRepositoryService easyRepositoryService

    @Override
    def executeInternal(def ctx) {

        def repositories = []
        List<EasyRepository> easyRepositories = easyRepositoryService.getRepositories()
        easyRepositories.each {
            def repository = [code: it.code, name: it.name]
            repositories.add(repository)
        }
        return JsonOutput.toJson(repositories)
    }
}
