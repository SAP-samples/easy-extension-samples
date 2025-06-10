import com.sap.cx.boosters.easy.extension.alternatecurrencyprice.mapper.PriceDataMapper
import com.sap.cx.boosters.easy.extension.alternatecurrencyprice.populator.ProductCryptoPricePopulator
import com.sap.cx.boosters.easy.extension.alternatecurrencyprice.priceconversion.service.impl.DefaultPriceConversionService
import de.hybris.platform.commercefacades.product.data.PriceData
import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO
import de.hybris.platform.converters.Populator
import de.hybris.platform.converters.impl.DefaultPopulatorList

logger.info "[${extension.id}] registering Spring beans for ..."

println "Registering core beans for ${extension.id}"

PriceData.metaClass.cryptoPrice = ''
PriceWsDTO.metaClass.cryptoPrice = ''

def addPopulator(String populatorList, Populator populator) {
    def matches = spring.getBean(populatorList, DefaultPopulatorList).populators.findAll { it.class.name == populator.class.name }
    populatorList.populators.removeAll(matches)
    populatorList.populators.add(populator)
}

easyCoreBeans {
    priceConversionService(DefaultPriceConversionService)

    productCryptoPricePopulator(ProductCryptoPricePopulator)

    addPopulator('defaultProductPricePopulatorList', productCryptoPricePopulator as Populator)

    registerAlias('defaultProductPricePopulatorList', 'productPricePopulatorList')

}

easyWebBeans('/occ/springmvc-v2') {
    priceDataMapper(PriceDataMapper) {
        it.parent = ref('abstractCustomMapper')
    }
    priceWsDTOFieldSetLevelMapping.levelMapping.DEFAULT = 'currencyIso,priceType,value,maxQuantity,minQuantity,formattedValue,cryptoPrice'
    priceWsDTOFieldSetLevelMapping.levelMapping.FULL = 'currencyIso,priceType,value,maxQuantity,minQuantity,formattedValue,cryptoPrice'
}

logger.info "[${extension.id}] beans registered ..."