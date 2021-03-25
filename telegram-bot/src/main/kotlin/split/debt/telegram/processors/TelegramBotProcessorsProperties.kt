package split.debt.telegram.processors

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConfigurationProperties("telegram.bot.processors")
@ConstructorBinding
internal data class TelegramBotProcessorsProperties(
    val languageCommandsTexts: Map<String, LanguageCommandTexts>,
)

data class LanguageCommandTexts(
    val commandsTexts: List<CommandConfig>,
    val defaultResponse: String,
)

data class CommandConfig(
    val commandText: String,
    val commandResponseTemplate: String,
)