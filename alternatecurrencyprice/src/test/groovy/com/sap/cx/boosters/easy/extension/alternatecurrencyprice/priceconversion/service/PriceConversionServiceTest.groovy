package com.sap.cx.boosters.easy.extension.alternatecurrencyprice.priceconversion.service

import com.sap.cx.boosters.easy.extension.alternatecurrencyprice.priceconversion.service.impl.DefaultPriceConversionService
import de.hybris.bootstrap.annotations.UnitTest
import de.hybris.platform.apiregistryservices.model.ConsumedDestinationModel
import de.hybris.platform.apiregistryservices.services.DestinationService
import de.hybris.platform.outboundservices.client.IntegrationRestTemplateFactory
import de.hybris.platform.servicelayer.config.ConfigurationService
import org.apache.commons.configuration.Configuration
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.springframework.web.client.RestTemplate

import static org.mockito.MockitoAnnotations.openMocks
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when

@UnitTest
class PriceConversionServiceTest {
    @InjectMocks
    PriceConversionService priceConversionService

    @Mock
    DestinationService<ConsumedDestinationModel> destinationService

    @Mock
    IntegrationRestTemplateFactory integrationRestTemplateFactory

    @Mock
    ConfigurationService configurationService

    @Before
    void setUp() {
        priceConversionService = new DefaultPriceConversionService()
        openMocks(this)

        def destination = mock(ConsumedDestinationModel)
        when(destinationService.getDestinationByIdAndByDestinationTargetId(DefaultPriceConversionService.COIN_GECKO_DESTINATION_ID, DefaultPriceConversionService.COIN_GECKO_DESTINATION_TARGET_ID)).thenReturn(destination)

        when(destination.getUrl()).thenReturn("https://api.coingecko.com/api/v3/simple/price")

        def restOperations = new RestTemplate()

        when(integrationRestTemplateFactory.create(destination)).thenReturn(restOperations)

        def configuration = mock(Configuration)

        when(configurationService.getConfiguration()).thenReturn(configuration)

        when(configuration.getString("coingecko.crypto.isocode.mapping.BTC")).thenReturn("bitcoin")
    }

    @Test
    void testGetCryptoPrice() {
        // Given
        BigDecimal price = new BigDecimal("100.00")
        String cryptoCurrency = "BTC"
        String priceCurrency = "GBP"

        // When
        BigDecimal convertedPrice = priceConversionService.getCryptoPrice(price, cryptoCurrency, priceCurrency)

        // Then
        assert convertedPrice != null
        assert convertedPrice > 0
    }
}
