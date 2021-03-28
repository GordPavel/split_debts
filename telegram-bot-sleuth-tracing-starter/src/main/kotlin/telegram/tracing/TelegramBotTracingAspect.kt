package telegram.tracing

import brave.propagation.TraceContext
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.springframework.cloud.sleuth.CurrentTraceContext
import org.springframework.cloud.sleuth.brave.bridge.BraveTraceContext
import java.util.concurrent.ThreadLocalRandom

@Aspect
internal class TelegramBotTracingAspect(
    private val currentTraceContext: CurrentTraceContext,
) {
    private val random = ThreadLocalRandom.current()

    @Before("execution(* org.telegram.telegrambots.meta.generics.*.onUpdatesReceived(..))")
    fun addTracingContext() {
        val traceId = random.nextLong()
        val traceContext = TraceContext.newBuilder()
            .traceId(traceId)
            .spanId(traceId)
            .parentId(null)
            .build()
        currentTraceContext.newScope(BraveTraceContext(traceContext))
    }

}