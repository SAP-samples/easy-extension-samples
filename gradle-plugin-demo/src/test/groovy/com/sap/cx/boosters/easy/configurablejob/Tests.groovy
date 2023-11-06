package com.sap.cx.boosters.easy.configurablejob


import de.hybris.platform.servicelayer.cronjob.impl.DefaultCronJobService
import spock.lang.Specification

class Tests extends Specification {

    def "dummy test"() {

        when: def res = 1 + 1

        then: res == 2

    }

    def "service dummy test"() {

        when:
        def cronJobService = new DefaultCronJobService()

        then:
        cronJobService

    }

}
