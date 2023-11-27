import com.sap.cx.boosters.easy.core.api.controllers.*
import com.sap.cx.boosters.easy.core.api.services.impl.DefaultEasyAPIService

logger.info "[${extension.id}] registering spring beans ..."

easyWebBeans('/easyrest') {
    easyAPIService(DefaultEasyAPIService)

    swaggerController(SwaggerController)

    getRepositoriesController(GetRepositoriesController)

    updateRepositoryController(UpdateRepositoryController)

    getExtensionsController(GetExtensionsController)

    installExtensionController(InstallExtensionController)

    reinstallExtensionController(ReInstallExtensionController)

    uninstallExtensionController(UninstallExtensionController)

    getEasyEventStatusController(GetEasyEventStatusController)
}

logger.info "[${extension.id}] registered spring beans ..."
