package split.debt.data

import reactor.core.publisher.Mono
import split.debt.core.Spending
import java.time.ZonedDateTime
import java.util.*

interface DataService {
    fun newParty(parentChatId: String?, name: String, created: ZonedDateTime): Mono<UUID>
    fun addUserToParty(partyId: UUID, userId: String, username: String): Mono<Void>
    fun addSpendingToParty(spending: Spending): Mono<UUID>
    fun deleteSpending(spendingId: UUID): Mono<Void>
}