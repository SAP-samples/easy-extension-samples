package com.sap.cx.boosters.easy.helloworld.service

import de.hybris.platform.servicelayer.config.ConfigurationService

import javax.annotation.Resource

class HelloWorldService {

    @Resource
    ConfigurationService configurationService

    String sayHello(firstName) {
        def message =  "${getMessage()} ${firstName}"
        return message
    }

    String getMessage() {
        return configurationService.getConfiguration().getString('easy.helloworld.message')
    }

}
