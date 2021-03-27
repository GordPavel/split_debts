package split.debt.data.neo4j

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import reactor.core.publisher.Mono
import spock.lang.Specification

import java.time.Clock
import java.time.Instant
import java.time.ZoneId

import static java.time.ZonedDateTime.now
import static org.assertj.core.api.Assertions.assertThat
import static split.debt.data.neo4j.Neo4jTestContainerConfig.NEO4J_BOLT_PORT
import static split.debt.data.neo4j.Neo4jTestContainerConfig.NEO4J_CONTAINER

@SpringBootTest(classes = [
        TestNeo4jDataServiceApplication,
        Neo4jTestContainerConfig,
])
class Neo4jDataServiceTest extends Specification {
    def static clock = Clock.fixed(Instant.parse('2000-01-01T00:00:00.000000Z'), ZoneId.of("Europe/Samara"))

    @Autowired
    PartyRepository partyRepository
    @Autowired
    PersonRepository personRepository
    @Autowired
    SpendingRepository spendingRepository
    @Autowired
    Neo4jDataService dataService

    @DynamicPropertySource
    static void initProperties(DynamicPropertyRegistry registry) {
        registry.add('spring.neo4j.uri') {
            "bolt://${NEO4J_CONTAINER.getContainerIpAddress()}:${NEO4J_CONTAINER.getMappedPort(NEO4J_BOLT_PORT)}"
        }
    }

    void setup() {
        Mono.zip(partyRepository.deleteAll(), personRepository.deleteAll(), spendingRepository.deleteAll()).block()
    }

    def 'Test correct save new party'() {
        when:
        def partyId = dataService.newParty(chatId, name, created).block()

        then:
        with(partyRepository.findById(partyId).block()) {
            id == partyId
            name == expectedName
            created == expectedCreated
            updated == expectedCreated
            chatId == expectedChatId
            closed == expectedClosed
            assertThat(spending).hasSameElementsAs(expectedSpending)
            assertThat(users).hasSameElementsAs(expectedUsers)
        }

        where:
        chatId   | name         | created    || expectedName | expectedCreated | expectedChatId | expectedClosed | expectedSpending | expectedUsers
        null     | 'testParty'  | now(clock) || 'testParty'  | now(clock)      | null           | false          | []               | []
        'chatId' | 'testParty1' | now(clock) || 'testParty1' | now(clock)      | 'chatId'       | false          | []               | []
    }

    def 'Test correct add new user to party'() {
        given:
        partyRepository.save(party).block()

        and:
        if (existedPerson != null) {
            personRepository.save(existedPerson).block()
        }

        when:
        dataService.addUserToParty(party.id, userId, username).block()

        then:
        def actualUsers = partyRepository.findById(party.id).map { it.users }.block()
        assertThat(actualUsers).hasSameElementsAs(expectedUsers)

        where:
        userId | username    | existedPerson               || expectedUsers
        '1'    | 'username'  | null                        || [new Person('1', 'username')]
        '2'    | 'username1' | new Person('2', 'username') || [new Person('1', 'username'), new Person('2', 'username')]
        party << [
                new Party(UUID.randomUUID(), 'party', now(clock), now(clock), null, false, [], []),
                new Party(UUID.randomUUID(), 'party', now(clock), now(clock), null, false, [], [new Person('1', 'username')])
        ]
    }

    def 'Test throws error if no party found on adding user to party by id'() {
        when:
        dataService.addUserToParty(partyId, '', ' ').block()

        then:
        def exception = thrown(PartyNotFoundException)
        exception.message == errorMessage

        where:
        partyId = UUID.randomUUID()
        errorMessage = "Can't find party for id ${partyId.toString()}"
    }
}
