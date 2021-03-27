package split.debt.data.neo4j

import java.util.*

internal class PartyNotFoundException(partyId: UUID) : IllegalArgumentException("Can't find party for id $partyId")