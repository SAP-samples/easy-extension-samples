package com.sap.cx.boosters.easy.easytutorialstep1.service.impl


import com.sap.cx.boosters.easy.easytutorialstep1.service.HelloWorldService

class DefaultHelloWorldService implements HelloWorldService{

    @Override
    String helloWorld() {
        return "Hello World!"
    }

}
