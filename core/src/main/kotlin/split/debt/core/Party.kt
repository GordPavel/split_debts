package split.debt.core

import com.fasterxml.jackson.annotation.JsonIdentityInfo
import com.fasterxml.jackson.annotation.ObjectIdGenerators.StringIdGenerator
import java.math.BigDecimal

@JsonIdentityInfo(scope = Person::class, generator = StringIdGenerator::class, property = "id")
data class Person(
    val id: String,
    val partyId: String,
    val name: String,
)

@JsonIdentityInfo(scope = Spending::class, generator = StringIdGenerator::class, property = "id")
data class Spending(
    val id: String,
    val partyId: String,
    val name: String,
    val payer: Person,
    val payees: List<Pair<BigDecimal, Person>>
)

@JsonIdentityInfo(scope = Transaction::class, generator = StringIdGenerator::class, property = "id")
data class Transaction(
    val id: String,
    val partyId: String,
    val payer: Person,
    val payee: Person,
    val amount: BigDecimal,
)
