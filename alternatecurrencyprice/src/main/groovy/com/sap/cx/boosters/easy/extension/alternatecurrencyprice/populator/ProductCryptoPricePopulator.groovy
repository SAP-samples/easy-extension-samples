package com.sap.cx.boosters.easy.extension.alternatecurrencyprice.populator

import com.sap.cx.boosters.easy.extension.alternatecurrencyprice.priceconversion.service.PriceConversionService
import de.hybris.platform.acceleratorfacades.order.data.PriceRangeData
import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator
import de.hybris.platform.commercefacades.product.data.PriceData
import de.hybris.platform.commercefacades.product.data.ProductData
import de.hybris.platform.core.model.c2l.CurrencyModel
import de.hybris.platform.core.model.product.ProductModel
import de.hybris.platform.servicelayer.dto.converter.ConversionException
import de.hybris.platform.store.services.BaseStoreService
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.annotation.Resource

class ProductCryptoPricePopulator<SOURCE extends ProductModel, TARGET extends ProductData> extends AbstractProductPopulator<SOURCE, TARGET> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductCryptoPricePopulator)

    @Resource
    private PriceConversionService priceConversionService

    @Resource
    private BaseStoreService baseStoreService

    @Override
    void populate(SOURCE source, TARGET target) throws ConversionException {
        LOG.info("Start :: Populating crypto price for product: ${source.code}")
        def baseStore = baseStoreService.getCurrentBaseStore()
        if (baseStore.hasProperty('cryptoCurrency')) {
            def price = target.getPrice()
            if(null!=price){
                LOG.info("Base store has 'cryptoCurrency' property set. Populating crypto price for product: ${source.code}")
                def cryptoCurrency = baseStore.cryptoCurrency as CurrencyModel
                if(cryptoCurrency) {
                    def cryptoPriceValue = priceConversionService.getCryptoPrice(price.value, price.currencyIso, cryptoCurrency.isocode)
                    price.cryptoPrice = "${cryptoCurrency.symbol ? cryptoCurrency.symbol : cryptoCurrency.isocode} ${cryptoPriceValue}"
                    // Temporarily setting the name to include crypto price in formatted price for testing purposes
                    LOG.info("Setting temprorary crypto price formatted value of price for product: ${source.code}")
                    price.formattedValue = price.cryptoPrice
                    LOG.info("Crypto price populated for product: ${source.code} - Crypto Price: ${price.cryptoPrice}")
                }else {
                    LOG.warn("Crypto currency is not set in base store for product: ${source.code}. Cannot populate crypto price.")
                }
            }else {
                LOG.warn("Price data is null for product: ${source.code}. Cannot populate crypto price.")
            }
        }else{
            LOG.warn("Base store does not have 'cryptoCurrency' property set. Skipping crypto price population for product: ${source.code}")
        }
        LOG.info("End :: Populating crypto price for product: ${source.code}")
    }
}
