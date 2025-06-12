import com.sap.cx.boosters.easy.extension.alternatecurrencyprice.mapper.impl.PriceMetaClassMapper
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

static def addPopulator(DefaultPopulatorList populatorList, Populator populator) {

    def matches = populatorList.populators.findAll { it.class.name == populator.class.name }
    populatorList.populators.removeAll(matches)
    populatorList.populators.add(populator)
}

easyCoreBeans {
    priceConversionService(DefaultPriceConversionService)

    productCryptoPricePopulator(ProductCryptoPricePopulator)

    addPopulator(defaultProductPricePopulatorList as DefaultPopulatorList, productCryptoPricePopulator as Populator)

    registerAlias('defaultProductPricePopulatorList', 'productPricePopulatorList')

}

easyWebBeans('/occ/springmvc-v2') {
    priceWsDTOFieldSetLevelMapping.levelMapping.DEFAULT = 'currencyIso,priceType,value,maxQuantity,minQuantity,formattedValue,cryptoPrice'
    priceWsDTOFieldSetLevelMapping.levelMapping.FULL = 'currencyIso,priceType,value,maxQuantity,minQuantity,formattedValue,cryptoPrice'

    priceMetaClassMapper(PriceMetaClassMapper){
        it.parent = ref('abstractCustomMapper')
    }
}

logger.info "[${extension.id}] beans registered ..."