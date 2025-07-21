package com.sap.cx.boosters.easy.extension.metaspacetest.mapper.impl


import de.hybris.platform.commercefacades.product.data.PriceData
import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO
import de.hybris.platform.webservicescommons.mapping.mappers.AbstractCustomMapper
import ma.glasnost.orika.MappingContext
import org.codehaus.groovy.reflection.ClassInfo
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PriceMetaClassMapper extends AbstractCustomMapper<PriceData, PriceWsDTO> {

    private static final Logger LOG = LoggerFactory.getLogger(PriceMetaClassMapper)

    @Override
    void mapAtoB(PriceData priceData, PriceWsDTO priceWsDTO, MappingContext context) {
        mapInternal(priceData, priceWsDTO)
    }

    @Override
    void mapBtoA(PriceWsDTO priceWsDTO, PriceData priceData, MappingContext context) {
        mapInternal(priceWsDTO, priceData)
    }

    private static <S, T> void mapInternal(S source, T target) {
        LOG.debug('mapping {} -> {}', source, target)

        def sourceProperties = ClassInfo.getClassInfo(source.class)?.modifiedExpando?.expandoProperties

        if (sourceProperties) {
            sourceProperties.each { mp ->
                LOG.debug('mapping meta property {}', mp.name)
                if (target.hasProperty(mp.name)) {
                    target[mp.name] = source[mp.name]
                }
            }

        }
    }
}
