logger.info "[${extension.id}] registering Spring beans for ..."

println "Registering core beans for ${extension.id}"

easyCoreBeans {

}

println "Registering easyrest beans for ${extension.id}"

easyWebBeans('/easyrest') {

}

logger.info "[${extension.id}] beans registered ..."
