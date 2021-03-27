package split.debt.data.neo4j

import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import split.debt.data.DataService
import java.time.ZonedDateTime
import java.util.*

internal class Neo4jDataService(
    private val partyRepository: PartyRepository,
    private val personRepository: PersonRepository,
    private val spendingRepository: SpendingRepository,
    private val partyMapper: Neo4jMapper,
) : DataService {
    override fun newParty(parentChatId: String?, name: String, created: ZonedDateTime) = Mono
        .just(Party(null, name, created, created, parentChatId, false, mutableListOf(), mutableListOf()))
        .flatMap(partyRepository::save)
        .map { it.id!! }

    override fun addUserToParty(partyId: UUID, userId: String, username: String) = partyRepository
        .findById(partyId)
        .switchIfEmpty(Mono.error(PartyNotFoundException(partyId)))
        .zipWith(
            personRepository.findById(userId)
                .switchIfEmpty { personRepository.save(Person(userId, username)) }
        )
        .flatMap { (party, user) -> partyRepository.save(party.apply { users.add(user) }) }
        .then()

    override fun addSpendingToParty(spending: split.debt.core.Spending) = Mono
        .just(
            Spending(
                null,
                spending.name,
                partyMapper.personToNeo4jDto(spending.payer),
                partyMapper.mapPayeesToEntity(spending)
            )
        )
        .flatMap(spendingRepository::save)
        .map { it.id!! }

    override fun deleteSpending(spendingId: UUID) = spendingRepository.deleteById(spendingId)

}