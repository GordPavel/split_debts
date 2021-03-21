package split.debt.core

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification
import spock.lang.Unroll

class SplitwiseServiceTest extends Specification {
    static final splitwiseService = new SplitwiseService()
    static final mapper = new ObjectMapper()
            .findAndRegisterModules()

    @Unroll
    def "Test splitwise works correct on #test"() {
        given:
        def party = mapper.readValue(getClass().getClassLoader().getResource(test), TestPartyObject)

        when:
        def transactions = splitwiseService.splitWise(party.spending)

        then:
        transactions == party.expectedTransactions

        where:
        test         | _
        'test1.json' | _
        'test2.json' | _
    }
}
