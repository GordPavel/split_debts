package split.debt.telegram.bot

import mu.KotlinLogging.logger
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import reactor.kotlin.core.publisher.toFlux
import split.debt.telegram.processors.DefaultResponseProcessor

private val logger = logger {}

@Service
internal class TelegramBot(
    private val botConfig: TelegramBotProperties,
    private val updateProcessors: Collection<TelegramUpdateProcessor<out BotApiMethod<Message>>>,
    private val defaultResponseProcessor: DefaultResponseProcessor,
    options: DefaultBotOptions,
) : TelegramLongPollingBot(options) {

    override fun onUpdateReceived(update: Update) {
        logger.info { "Received update: $update" }

        updateProcessors.toFlux()
            .flatMap { it.processUpdate(update) }
            .switchIfEmpty {
                defaultResponseProcessor.processUpdate(update)
                    .subscribe(it::onNext)
            }
            .doOnNext { logger.info { "Reply $it" } }
            .switchIfEmpty { logger.info { "No reply" } }
            .subscribe { execute(it) }
    }

    override fun getBotToken() = botConfig.token

    override fun getBotUsername() = botConfig.username
}