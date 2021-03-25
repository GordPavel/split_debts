package split.debt.telegram.bot

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("telegram.bot")
@ConstructorBinding
internal data class TelegramBotProperties(
    val token: String,
    val username: String,
)
