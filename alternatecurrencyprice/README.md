# Alternate Currency Price

This Easy Extension demonstrates the product price availability in Crypto currency.

## Use Case
The business users would like to provide the equivalent price of the products in the Crypto currency, such as Bitcoin, in addition to the standard currency (e.g., Euro, US Dollar) in the storefront.

The business user configures the Crypto currency for the base store in the Backoffice, and the storefront displays the product price in both the standard and configured Crypto currency.

## Implementation
This section explains how to implement the alternate currency price functionality in SAP Commerce Cloud.
1. **Store's Crypto Currency**: 
   For this the `cryptoCurrency` attribute is added to the `BaseStore` item type via [Type Descriptor](./src/main/resources/easytypes.json) as.
    ```json
   {
    "itemtypes": [
        {
            "code": "BaseStore",
            "autocreate": "false",
            "generate": "false",
            "attributes": [
                {
                    "qualifier": "cryptoCurrency",
                    "type": "Currency",
                    "typeClass": "de.hybris.platform.core.model.c2l.CurrencyModel",
                    "name": [
                        {
                            "lang": "en",
                            "value": "Crypto Currency"
                        }
                      ],
                    "persistence": {
                        "type": "property",
                        "column": "p_cryptoCurrency"
                      },
                    "modifiers": {
                        "unique": "false",
                        "initial": "false",
                        "optional": "true",
                        "write": "true",
                        "partof": "false"
                      }
                    }
                ]
            }
        ]
    }
    ```
2. **Currency Conversion**: 
   The extension uses the [Crypto Currency Converter](./src/main/java/org/sap/cc/alternatecurrencyprice/converter/CryptoCurrencyConverter.java) to convert the product price from the standard currency to the configured Crypto currency. The converter uses the [Crypto Currency Service](./src/main/groovy/com/sap/cx/boosters/easy/extension/alternatecurrencyprice/priceconversion/service/impl/DefaultPriceConversionService.groovy) to fetch the current exchange rate (and cache for 30 mins) from `Coin Gecko` for the Crypto currency and converts the product price from standard currency to crypto price as:
    ```groovy
    import com.sap.cx.boosters.easy.extension.alternatecurrencyprice.priceconversion.service.PriceConversionService
    import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
    import de.hybris.platform.apiregistryservices.services.DestinationService
    import de.hybris.platform.outboundservices.client.IntegrationRestTemplateFactory
    import de.hybris.platform.servicelayer.config.ConfigurationService
    import org.slf4j.Logger
    import org.slf4j.LoggerFactory
    import org.springframework.http.HttpEntity
    import org.springframework.http.HttpHeaders
    import org.springframework.http.HttpMethod
    import org.springframework.http.MediaType
    import jakarta.annotation.Resource
    import java.time.Instant
    import java.util.concurrent.ConcurrentHashMap
    
    class DefaultPriceConversionService implements PriceConversionService {
    
        private static final Logger LOG = LoggerFactory.getLogger(DefaultPriceConversionService)
    
        static final COIN_GECKO_DESTINATION_ID = "coinGeckoDestination"
    
        static final COIN_GECKO_DESTINATION_TARGET_ID = "coinGeckoDestinationTarget"
    
        // Cache structure: key -> [rate: BigDecimal, timestamp: Instant]
        private final ConcurrentHashMap<String, Map> cache = new ConcurrentHashMap<>()
    
        // TTL in milliseconds (30 minutes)
        static final long CACHE_TTL_MS = 30 * 60 * 1000
    
        @Resource
        private DestinationService<ConsumedDestinationModel> destinationService
    
        @Resource
        private IntegrationRestTemplateFactory integrationRestTemplateFactory
    
        @Resource
        private ConfigurationService configurationService
    
        /**
         * Converts the given price from one currency to another.
         *
         * @param price the price to convert
         * @param cryptoCurrency the currency of the price
         * @param priceCurrency the currency to convert to
         * @return the converted price
         */
        @Override
        BigDecimal getCryptoPrice(BigDecimal price, String priceCurrency, String cryptoCurrency) {
            String cacheKey = "${cryptoCurrency}_${priceCurrency}".toLowerCase()
    
            // Check if cached and valid
            if (cache.containsKey(cacheKey)) {
                Map cachedEntry = cache[cacheKey]
                Instant cachedAt = cachedEntry.timestamp as Instant
                if (Instant.now().toEpochMilli() - cachedAt.toEpochMilli() < CACHE_TTL_MS) {
                    // Use cached rate
                    BigDecimal cachedRate = cachedEntry.rate as BigDecimal
                    LOG.info("Using cached exchange rate for $cacheKey: $cachedRate")
                    def convertedPrice = price / cachedRate
                    LOG.info("Converted price using cached rate: {} {} = {} {}", price, priceCurrency, convertedPrice, cryptoCurrency)
    
                    return convertedPrice
                }
            }
    
            // Cache miss or expired, fetch new rate from API
            BigDecimal freshRate = getExchangeRate(cryptoCurrency, priceCurrency)
    
            // Update cache
            cache[cacheKey] = [rate: freshRate, timestamp: Instant.now()]
    
            LOG.info("Fetched new exchange rate for $cacheKey: $freshRate")
    
            def convertedPrice = price / freshRate
            LOG.info("Converted price using fresh rate: {} {} = {} {}", price, priceCurrency, convertedPrice, cryptoCurrency)
    
            return convertedPrice
        }
    
        /**
         * Gets the exchange rate between two currencies.
         *
         * @param from the source currency ISO code
         * @param to the target currency ISO code
         * @return the exchange rate
         */
        BigDecimal getExchangeRate(String from, String to) {
            LOG.info("Fetching exchange rate from CoinGecko API for {} -> {}", from, to)
    
            def cryptoId = configurationService.getConfiguration().getString(String.format("coingecko.crypto.isocode.mapping.%s", from))
    
            def destination = destinationService.getDestinationByIdAndByDestinationTargetId(COIN_GECKO_DESTINATION_ID, COIN_GECKO_DESTINATION_TARGET_ID)
    
            def restOperations = integrationRestTemplateFactory.create(destination)
            def url = "$destination.url?ids=${cryptoId.toLowerCase()}&vs_currencies=${to.toLowerCase()}"
    
            def headers = new HttpHeaders()
            headers.setContentType(MediaType.APPLICATION_JSON)
            def requestEntity = new HttpEntity<>(headers)
    
            final HttpEntity<Map> entity = restOperations.exchange(url, HttpMethod.GET, requestEntity, Map)
    
            def rate = entity.body[cryptoId.toLowerCase()][to.toLowerCase()]
            LOG.info("Exchange rate fetched: {} {} = {} {}", 1, from, rate, to)
            return new BigDecimal(rate.toString())
        }
    }

    ```
3. **Populate Crypto Price**:
   In this step, the `ProductCryptoPricePopulator` is implemented to populate the Crypto price in the product model. The populator uses the `CryptoCurrencyConverter` to convert the product price from the standard currency to the configured Crypto currency and sets it in the product model as:
    ```groovy
    import com.sap.cx.boosters.easy.extension.alternatecurrencyprice.priceconversion.service.PriceConversionService
    import de.hybris.platform.commercefacades.product.converters.populator.AbstractProductPopulator
    import de.hybris.platform.commercefacades.product.data.ProductData
    import de.hybris.platform.core.model.c2l.CurrencyModel
    import de.hybris.platform.core.model.product.ProductModel
    import de.hybris.platform.servicelayer.dto.converter.ConversionException
    import de.hybris.platform.store.services.BaseStoreService
    import org.slf4j.Logger
    import org.slf4j.LoggerFactory
    
    import jakarta.annotation.Resource
    
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
                        price.cryptoPrice = "${cryptoCurrency.symbol ?cryptoCurrency.symbol: cryptoCurrency.isocode} ${priceConversionService.getCryptoPrice(price.value, price.currencyIso, cryptoCurrency.isocode)}"
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

    ```
4. **Custom Orika Mapper**:
    The `PriceDataMapper` is implemented to map the Crypto price from the product model to the `PriceWsDTO`. This mapper ensures that the Crypto price is included in the Web Service DTO as:
     ```groovy
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

     ```
5. **Beans Configuration**:
    In this step we;
    - Configure the custom properties in ProductPriceData and PriceWsDTO to hold the Crypto price using `Groovy` metaclass.
    - Adding the newly created price populator to the `productPricePopulatorList` to ensure it is invoked during product price population.
    - By re-registering the alias we ensure that the changes reflect everywhere wherever it is invoked.
    - configure the spring beans of the classes implemented above in `EasyBeans.groovy` as:
    ```groovy
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
    ```
5. **Configurations**:
    The WsDTOs are mapped by Orika Mappers, so we need to ensure that the properties are defined for field sets and mapper is configured. The mapper is configured in `EasyBeans.groovy` for `occ v2` context. However, as the `Groovy` metaclass added attributes are untypes, we need to defined the types for the properties added as:
    ```properties
    easy.occ.field.type.de.hybris.platform.commercewebservicescommons.dto.product.PriceWsDTO.cryptoPrice = java.lang.String
    ```
6. **Install ImpEx Configurations**
    The impex configurations are added to assign the cypto currency to the base store and configuring the Gecko Coin API details in API Registry as:
    ```impex
    INSERT_UPDATE Currency;isocode[unique=true];name[lang=en];conversion;digits;symbol
    ;BTC;"Bitcoin";1;9;₿
    ;ETH;"Ethereum";1;9;Ξ
    
    UPDATE BaseStore;uid[unique=true];cryptoCurrency(isocode)
    ;electronics;BTC
    
    #******************#
    #***  Endpoints ***#
    #******************#
    INSERT_UPDATE Endpoint;id[unique=true];version[unique=true];specUrl;name
    ;coinGeckoEnpoint;3;https://api.coingecko.com/api/v3/simple/price;coinGeckoEnpoint
    
    #****************************#
    #***  Destination Target  ***#
    #****************************#
    INSERT_UPDATE DestinationTarget;id[unique=true]
    ;coinGeckoDestinationTarget;
    
    #*******************************#
    #***  Consumed Destinations  ***#
    #*******************************#
    INSERT_UPDATE ConsumedDestination;id[unique=true];url;endpoint(id,version);credential(id);active;destinationTarget(id)[default=coinGeckoDestinationTarget]
    ;coinGeckoDestination;https://api.coingecko.com/api/v3/simple/price;coinGeckoEnpoint:3;;true
   ```

7. **Uninstall ImpEx Configurations**
    The impex configurations are added to remove the Gecko Coin API details in API Registry as:
    ```impex
    #*******************************#
    #***  Consumed Destinations  ***#
    #*******************************#
    REMOVE ConsumedDestination;id[unique=true]
    ;coinGeckoDestination
    
    #****************************#
    #***  Destination Target  ***#
    #****************************#
    REMOVE DestinationTarget;id[unique=true]
    ;coinGeckoDestinationTarget;
    
    #******************#
    #***  Endpoints ***#
    #******************#
    REMOVE Endpoint;id[unique=true];version[unique=true]
    ;coinGeckoEnpoint;3
    ```
6. **Testing**:
    - Build the extension and deploy it to your SAP Commerce Cloud instance.
    - Configure a base store with a Crypto currency in the Backoffice.
    - Access the OCC product API to validate that the crypto price is populated in the result.
    - You can now implement the composable storefront for the display using the value available in the OCC response.

>**Note:** This extension requires [Easy Extension Framework](https://sap.github.io/easy-extension-framework) installed on top of SAP Commerce Cloud.