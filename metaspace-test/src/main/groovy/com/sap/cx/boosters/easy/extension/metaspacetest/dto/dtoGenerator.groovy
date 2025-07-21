package com.sap.cx.boosters.easy.extension.metaspacetest.dto


def generateClasses() {
    def basePath = './src/main/groovy/com/sap/cx/boosters/easy/extension/metaspacetest/dto/'
    def classTemplate = { className ->
        """package com.sap.cx.boosters.easy.extension.metaspacetest.dto


class ${className} {
    private Long ID
    private String name
    private String description

    Long getID() {
        return ID
    }

    void setID(Long ID) {
        this.ID = ID
    }

    String getName() {
        return name
    }

    void setName(String name) {
        this.name = name
    }

    String getDescription() {
        return description
    }

    void setDescription(String description) {
        this.description = description
    }
}

"""
    }

    (1..1000).each { i ->
        def className = "MetaspaceTestDTO${i}"
        def filePath = new File(basePath + "${className}.groovy")
        filePath.text = classTemplate(className)
    }
}

generateClasses()
