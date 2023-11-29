package com.sap.cx.boosters.easy.easytutorialstep4.service.impl


import com.sap.cx.boosters.easy.easytutorialstep4.service.HelloWorldService

class DefaultHelloWorldService implements HelloWorldService{

    @Override
    String helloWorld() {
        return "Hello World!"
    }

}
