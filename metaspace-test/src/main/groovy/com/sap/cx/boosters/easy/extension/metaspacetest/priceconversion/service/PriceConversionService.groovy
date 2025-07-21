package com.sap.cx.boosters.easy.extension.metaspacetest.priceconversion.service

interface PriceConversionService {

    /**
     * Converts the given price from one currency to another.
     *
     * @param price the price to convert
     * @param cryptoCurrency the currency of the price
     * @param priceCurrency the currency to convert to
     * @return the converted price
     */
    BigDecimal getCryptoPrice(BigDecimal price, String cryptoCurrency, String priceCurrency)

}