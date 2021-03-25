package split.debt.telegram

import com.github.tomakehurst.wiremock.WireMockServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.TestPropertySource
import org.telegram.telegrambots.ApiContextInitializer
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook
import org.telegram.telegrambots.meta.api.methods.updates.GetUpdates
import spock.lang.Specification

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig
import static java.util.Objects.nonNull

@SpringBootTest(classes = TelegramBotApplication)
@ActiveProfiles('test')
@TestPropertySource(
        properties = [
                'telegram.bot.url=http://localhost:61226/',
        ]
)
class ContextStartsTest extends Specification {

    static def wireMockServer = new WireMockServer(wireMockConfig()
            .bindAddress('localhost')
            .port(61226)
    )

    def setupSpec() {
        wireMockServer.start()
        wireMockServer.stubFor(
                post(urlEqualTo("/test_token/$DeleteWebhook.PATH"))
                        .willReturn(aResponse()
                                .withBodyFile('wiremock/stubs/deleteWebhookSuccessResponse.json'))
        )
        wireMockServer.stubFor(
                post(urlEqualTo("/test_token/$GetUpdates.PATH"))
                        .willReturn(aResponse()
                                .withBodyFile('wiremock/stubs/getUpdatedEmptyResponse.json'))
        )
        ApiContextInitializer.init()
    }

    def cleanupSpec() {
        wireMockServer.stop()
    }

    @Autowired
    TelegramBotApplication application

    def "contextLoads"() {
        expect:
        nonNull(application)
    }
}
