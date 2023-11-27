package com.sap.cx.boosters.easy.helloworld.junit

import io.restassured.RestAssured
import org.junit.Before
import org.junit.Test

import static io.restassured.RestAssured.when
import static org.hamcrest.Matchers.equalTo

class HelloWorldControllerTest {

    def baseUrl = 'https://localhost:9002/easyrest'

    @Before
    void setUp() {
        RestAssured.useRelaxedHTTPSValidation()
    }

    @Test
    void test_valid_get() {

        when().get("${baseUrl}/easyHelloWorld?firstname={firstname}", 'Yannick')
                .then()
                .body(equalTo('{"message":"hello Yannick"}'))
                .statusCode(200)

    }

}

