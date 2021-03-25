package split.debt.telegram.bot

import mu.KotlinLogging.logger
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.objects.Update

private val logger = logger {}

@Service
internal class TelegramBot(
    private val botConfig: TelegramBotProperties,
    options: DefaultBotOptions,
) : TelegramLongPollingBot(options) {

    override fun onUpdateReceived(update: Update) {
        logger.info { "Received update: $update" }
    }

    override fun getBotToken() = botConfig.token

    override fun getBotUsername() = botConfig.username
}