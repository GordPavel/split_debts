package split.debt.telegram.bot

import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import reactor.core.publisher.Mono

interface TelegramUpdateProcessor<Method : BotApiMethod<out Message>> {
    fun processUpdate(update: Update): Mono<Method>
}