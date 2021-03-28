package split.debt.telegram.bot

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.ApiConstants

@Configuration
internal class TelegramBotConfiguration {
    @Bean
    fun botOptions(
        @Value("\${telegram.bot.url:#{null}}") telegramUrl: String?
    ) = DefaultBotOptions().apply {
        baseUrl = telegramUrl ?: ApiConstants.BASE_URL
    }
}