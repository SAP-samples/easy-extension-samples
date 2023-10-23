package com.sap.cx.boosters.easy.delivery

import spock.lang.Specification
import java.nio.file.Files

class GroovyEngineTests extends Specification {

    def classCodeA = '''
    package com.sap.cx.easy.tests
    class TestMe {
      def s = 'A'
    }'''

    def classCodeB = '''
    package com.sap.cx.easy.tests
    class TestMe {
      def s = 'B'
    }'''

    def script = 'new com.sap.cx.easy.tests.TestMe()'

    def "check classes"() {

        when:
        String dirA = Files.createTempDirectory('easyTemp').toFile()
        println dirA
        String dirB = Files.createTempDirectory('easyTemp').toFile()
        println dirB

        then:
        true

    }

}
