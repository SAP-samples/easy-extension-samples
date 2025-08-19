package com.sap.cx.boosters.easy.extension.alternatecurrencyprice.priceconversion.service.impl

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
