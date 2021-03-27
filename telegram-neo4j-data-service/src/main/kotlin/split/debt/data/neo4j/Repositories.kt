package split.debt.data.neo4j

import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository
import java.util.*

internal interface PartyRepository : ReactiveNeo4jRepository<Party, UUID>

internal interface PersonRepository : ReactiveNeo4jRepository<Person, String>

internal interface SpendingRepository : ReactiveNeo4jRepository<Spending, UUID>