import com.sap.cx.boosters.easy.extension.alternatecurrencyprice.populator.ProductCryptoPricePopulator
import com.sap.cx.boosters.easy.extension.alternatecurrencyprice.priceconversion.service.impl.DefaultPriceConversionService
import de.hybris.platform.commercefacades.product.data.PriceData
import de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO

logger.info "[${extension.id}] registering Spring beans for ..."

println "Registering core beans for ${extension.id}"

PriceData.metaClass.cryptoPrice = ''
PriceWsDTO.metaClass.cryptoPrice = ''

easyCoreBeans {
    priceConversionService(DefaultPriceConversionService)

    productCryptoPricePopulator(ProductCryptoPricePopulator) {
        it.parent = ref('defaultProductPricePopulator')
    }
    registerAlias('productCryptoPricePopulator', 'productPricePopulator')


}

easyWebBeans('/occ/springmvc-v2') {
    priceWsDTOFieldSetLevelMapping.levelMapping.DEFAULT = 'currencyIso,priceType,value,maxQuantity,minQuantity,formattedValue,cryptoPrice'
    priceWsDTOFieldSetLevelMapping.levelMapping.FULL = 'currencyIso,priceType,value,maxQuantity,minQuantity,formattedValue,cryptoPrice'
}

logger.info "[${extension.id}] beans registered ..."