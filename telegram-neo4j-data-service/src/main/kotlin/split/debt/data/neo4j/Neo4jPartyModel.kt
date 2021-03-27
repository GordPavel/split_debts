package split.debt.data.neo4j

import org.springframework.data.neo4j.core.schema.*
import org.springframework.data.neo4j.core.schema.GeneratedValue.UUIDGenerator
import org.springframework.data.neo4j.core.schema.Relationship.Direction.INCOMING
import org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.*

@Node("party")
internal data class Party(
    @Id @GeneratedValue(UUIDGenerator::class)
    val id: UUID?,
    val name: String,
    val created: ZonedDateTime,
    val updated: ZonedDateTime,
    val chatId: String?,
    val closed: Boolean,
    @Relationship(type = "PAYED_IN", direction = INCOMING)
    val spending: MutableCollection<Spending>,
    @Relationship(type = "ACTED_IN", direction = INCOMING)
    val users: MutableCollection<Person>
)

@Node("person")
internal data class Person(
    @Id
    val id: String,
    val name: String,
)

@Node("spending")
internal data class Spending(
    @Id @GeneratedValue(UUIDGenerator::class)
    val id: UUID?,
    val name: String,
    @Relationship(type = "PAYED", direction = OUTGOING)
    val payer: Person,
    @Relationship(type = "PAYED_FOR", direction = OUTGOING)
    val payees: List<Payee>
)

@RelationshipProperties
internal data class Payee(
    @Id @GeneratedValue(UUIDGenerator::class)
    val id: UUID?,
    val amount: BigDecimal,
    @TargetNode
    val payee: Person,
)