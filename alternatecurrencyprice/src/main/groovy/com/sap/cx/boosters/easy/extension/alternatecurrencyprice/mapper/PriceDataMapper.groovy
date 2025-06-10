package com.sap.cx.boosters.easy.extension.alternatecurrencyprice.mapper

import de.hybris.platform.commercefacades.product.data.PriceData
import de.hybris.platform.commercefacades.product.data.PriceDataType
import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO
import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTOType
import de.hybris.platform.webservicescommons.mapping.mappers.AbstractCustomMapper
import ma.glasnost.orika.MappingContext

class PriceDataMapper extends AbstractCustomMapper<PriceData, PriceWsDTO> {

    @Override
    void mapAtoB(PriceData source, PriceWsDTO target, MappingContext context) {
        if (source != null && target != null) {
            target.value = source.value
            target.currencyIso = source.currencyIso
            target.formattedValue = source.formattedValue
            target.maxQuantity = source.maxQuantity
            target.minQuantity = source.minQuantity
            target.priceType = mapperFacade.map(source.priceType, PriceWsDTOType)
            target.cryptoPrice = source.cryptoPrice
        }
    }

    @Override
    void mapBtoA(PriceWsDTO source, PriceData target, MappingContext context) {
        if (source != null && target != null) {
            if (source != null && target != null) {
                target.value = source.value
                target.currencyIso = source.currencyIso
                target.formattedValue = source.formattedValue
                target.maxQuantity = source.maxQuantity
                target.minQuantity = source.minQuantity
                target.priceType = mapperFacade.map(source.priceType, PriceDataType)
                target.cryptoPrice = source.cryptoPrice
            }
        }
    }
}
