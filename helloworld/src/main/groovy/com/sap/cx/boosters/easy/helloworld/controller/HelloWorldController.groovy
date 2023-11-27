package com.sap.cx.boosters.easy.helloworld.controller

import com.sap.cx.boosters.easy.helloworld.service.HelloWorldService
import com.sap.cx.boosters.easyrest.controller.EasyRestServiceController

import javax.annotation.Resource

class HelloWorldController implements EasyRestServiceController {

    @Resource
    HelloWorldService helloWorldService

    @Override
    Map execute(Map ctx) {
        def response = [:]
        response.'responseCode' = 200
        response.'body' = groovy.json.JsonOutput.toJson([message:helloWorldService.sayHello(ctx.parameters.firstname)])
        return response
    }

}
