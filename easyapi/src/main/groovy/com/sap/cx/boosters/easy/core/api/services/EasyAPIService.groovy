package com.sap.cx.boosters.easy.core.api.services

interface EasyAPIService {
    boolean isValidAPIKey(Map ctx)

    Map<String, Object> buildInvalidAPIKeyResponse()

}
