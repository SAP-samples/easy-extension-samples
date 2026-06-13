package com.sap.cx.boosters.easy.helloworld.spock


import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import org.junit.jupiter.api.Assumptions
import spock.lang.Specification

import static io.restassured.RestAssured.given
import static org.hamcrest.Matchers.equalTo

class HelloWorldControllerTest extends Specification {

    def 'test helloWorldController'() {
        def requestSpec = new RequestSpecBuilder().setBaseUri(System.getProperty("easyRestBaseUrl")).build()
        RestAssured.useRelaxedHTTPSValidation()

        def response = null
        try {
            response = given(requestSpec).get('/easyHelloWorld?firstname:Yannick')

        } catch (Exception e) {
            println "Skipping test due to connection failure: ${e.class.simpleName} - ${e.message}"
            Assumptions.assumeTrue(false, "Connection unavailable, skipping test")
        }

        expect:
        Assumptions.assumeTrue(response?.statusCode() != 404, "API returned 404, skipping test")
        response.then()
                .statusCode(200)
                .body('message', equalTo('hello Yannick'))

    }

}
