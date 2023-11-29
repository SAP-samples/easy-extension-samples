package com.sap.cx.boosters.easy.easytutorialstep6.service.impl


import com.sap.cx.boosters.easy.easytutorialstep6.service.HelloWorldService


class DefaultHelloWorldService implements HelloWorldService{

    @Override
    String helloWorld() {
        return "Hello World!"
    }

}
