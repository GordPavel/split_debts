package telegram.tracing

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.cloud.sleuth.CurrentTraceContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConditionalOnProperty(
    "telegram.bot.tracing.enabled",
    matchIfMissing = true
)
@ConditionalOnBean(CurrentTraceContext::class)
internal class TelegramBotTracingConfiguration {

    @Bean
    fun telegramBotTracingAspect(currentTraceContext: CurrentTraceContext) =
        TelegramBotTracingAspect(currentTraceContext)

}