/*
 * This Spock specification was generated by the Gradle 'init' task.
 */
package com.sap.cx.boosters.easy.easytutorialstep6

import io.restassured.RestAssured
import io.restassured.builder.RequestSpecBuilder
import org.junit.Test
import spock.lang.Specification

import static io.restassured.RestAssured.given
import static org.apache.http.HttpStatus.SC_OK

class EasyRestTest extends Specification {
    def requestSpec = new RequestSpecBuilder().setBaseUri(System.getProperty("easyRestBaseUrl")).build()

    @Test
    void "test availableSlots"() {
        RestAssured.useRelaxedHTTPSValidation()
        given:
        def request = given(requestSpec)

        when:
        def response = request.get('electronics-spa/users/anonymous/carts/testDeliverySlotCartGUID/getAvailableSlots')

        then:
        response.then()
                .statusCode(SC_OK)

    }
}
