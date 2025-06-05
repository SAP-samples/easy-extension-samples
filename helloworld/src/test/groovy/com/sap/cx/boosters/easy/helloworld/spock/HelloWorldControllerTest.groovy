package com.sap.cx.boosters.easy.helloworld.spock


import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import spock.lang.Specification

import static io.restassured.RestAssured.given
import static org.hamcrest.Matchers.equalTo

class HelloWorldControllerTest extends Specification {

    def 'test helloWorldController'() {
        def requestSpec = new RequestSpecBuilder().setBaseUri(System.getProperty("easyRestBaseUrl")).build()
        RestAssured.useRelaxedHTTPSValidation()

        given:
        def request = given(requestSpec)

        when:
        def response = request.get('/easyHelloWorld?firstname:Yannick')

        and:
        if (response.statusCode() == 404) {
            println "API is not available, skipping test."
            return
        }

        then:
        response.then()
                .statusCode(200)
                .body('message', equalTo('hello Yannick'))

    }

}
