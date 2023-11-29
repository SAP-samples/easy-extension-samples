package com.sap.cx.boosters.easy.easytutorialstep5.service.impl


import com.sap.cx.boosters.easy.easytutorialstep5.service.HelloWorldService


class DefaultHelloWorldService implements HelloWorldService{

    @Override
    String helloWorld() {
        return "Hello World!"
    }

}
