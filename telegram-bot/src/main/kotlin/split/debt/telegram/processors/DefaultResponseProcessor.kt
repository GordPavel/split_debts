package split.debt.telegram.processors

import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@Service
internal class DefaultResponseProcessor(
    private val texts: TelegramBotProcessorsProperties,
) {
    fun processUpdate(update: Update) = Mono.just(update)
        .filter { it.message?.isUserMessage ?: false }
        .flatMap {
            texts.languageCommandsTexts[update.message.from.languageCode]?.defaultResponse?.let { response ->
                SendMessage(it.message.chatId, response)
            }.toMono()
        }
}