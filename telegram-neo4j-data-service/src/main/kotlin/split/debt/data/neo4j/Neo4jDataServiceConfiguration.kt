package split.debt.data.neo4j

import org.mapstruct.factory.Mappers.getMapper
import org.neo4j.driver.Driver
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.neo4j.core.ReactiveDatabaseSelectionProvider
import org.springframework.data.neo4j.core.transaction.ReactiveNeo4jTransactionManager
import org.springframework.data.neo4j.repository.config.EnableReactiveNeo4jRepositories
import org.springframework.data.neo4j.repository.config.ReactiveNeo4jRepositoryConfigurationExtension.DEFAULT_TRANSACTION_MANAGER_BEAN_NAME

@Configuration
@ConditionalOnProperty("data.neo4j.enabled", matchIfMissing = true)
@EnableReactiveNeo4jRepositories("split.debt.data.neo4j")
class Neo4jDataServiceConfiguration {

    @Bean(DEFAULT_TRANSACTION_MANAGER_BEAN_NAME)
    fun reactiveTransactionManager(driver: Driver, databaseNameProvider: ReactiveDatabaseSelectionProvider) =
        ReactiveNeo4jTransactionManager(driver, databaseNameProvider)

    @Bean
    internal fun neo4jDataService(
        partyRepository: PartyRepository,
        personRepository: PersonRepository,
        spendingRepository: SpendingRepository,
    ) = Neo4jDataService(
        partyRepository,
        personRepository,
        spendingRepository,
        neo4jPartyMapper(),
    )

    @Bean
    internal fun neo4jPartyMapper() = getMapper(Neo4jMapper::class.java)!!

}