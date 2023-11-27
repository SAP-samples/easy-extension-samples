package com.sap.cx.boosters.easy.helloworld.spock

import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import spock.lang.Specification
import static org.apache.http.HttpStatus.SC_OK

class HelloWorldControllerTest extends Specification {

    def restClient = new RESTClient( 'https://localhost:9002')

    def 'test helloWorldController'() {

        when:
        restClient.ignoreSSLIssues()
        HttpResponseDecorator response = restClient.get(path: '/easyrest/easyHelloWorld', query:[firstname:'Yannick'])

        then:
        with(response) {
            status == SC_OK
            data == [message:'hello Yannick']

        }

    }

}
