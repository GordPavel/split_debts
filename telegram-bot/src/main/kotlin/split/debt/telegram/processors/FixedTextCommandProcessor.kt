package split.debt.telegram.processors

import mu.KotlinLogging.logger
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.ParseMode
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono
import split.debt.telegram.bot.TelegramUpdateProcessor

private val logger = logger {}

@Service
@Order(Ordered.HIGHEST_PRECEDENCE)
internal class FixedTextCommandProcessor(
    texts: TelegramBotProcessorsProperties,
) : TelegramUpdateProcessor<SendMessage> {

    private val commands: Map<String, Map<String, String>> = texts.languageCommandsTexts
        .mapValues { (_, languageConfig) ->
            languageConfig.commandsTexts
                .filter { !it.commandResponseTemplate.contains("%s") }
                .associateBy(CommandConfig::commandText, CommandConfig::commandResponseTemplate)
        }
        .also { logger.info { "Load $it texts" } }

    override fun processUpdate(update: Update): Mono<SendMessage> =
        commands[update.message.from.languageCode]?.let { commandConfig ->
            commandConfig[update.message.text]?.let { response ->
                SendMessage(update.message.chatId, response).apply { setParseMode(ParseMode.MARKDOWN) }
            }
        }.toMono()
}