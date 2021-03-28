package split.debt.telegram

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.telegram.telegrambots.ApiContextInitializer
import split.debt.core.SplitwiseService

@SpringBootApplication
@ConfigurationPropertiesScan
class TelegramBotApplication {
    @Bean
    fun splitwiseService() = SplitwiseService()
}

fun main(args: Array<String>) {
    ApiContextInitializer.init()
    runApplication<TelegramBotApplication>(*args)
}
