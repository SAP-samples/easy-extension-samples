package com.sap.cx.boosters.easy.delivery.service

class HelloWorldService {

    def LOG = org.slf4j.LoggerFactory.getLogger('HelloWorldService')

    String helloWorld(){
        return 'Hello World!'
    }

}
