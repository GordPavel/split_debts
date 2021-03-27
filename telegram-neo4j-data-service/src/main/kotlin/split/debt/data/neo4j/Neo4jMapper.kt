package split.debt.data.neo4j

import org.mapstruct.Mapper
import split.debt.core.Person
import split.debt.core.Spending

@Mapper
internal abstract class Neo4jMapper {

    abstract fun personToNeo4jDto(person: Person): split.debt.data.neo4j.Person

    fun mapPayeesToEntity(spending: Spending) =
        spending.payees.asSequence().map { Payee(null, it.first, personToNeo4jDto(it.second)) }.toList()

}