package telegram.tracing

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cloud.sleuth.CurrentTraceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(
    "telegram.bot.tracing.enabled",
    matchIfMissing = true
)
internal class TelegramBotTracingConfiguration {

    @Autowired
    private lateinit var currentTraceContext: CurrentTraceContext

    @Bean
    fun telegramBotTracingAspect() = TelegramBotTracingAspect(currentTraceContext)

}