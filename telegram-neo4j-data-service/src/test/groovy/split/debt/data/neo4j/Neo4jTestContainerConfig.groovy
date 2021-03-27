package split.debt.data.neo4j

import org.springframework.boot.test.context.TestConfiguration
import org.testcontainers.containers.Neo4jContainer
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy
import org.testcontainers.utility.DockerImageName

@TestConfiguration
class Neo4jTestContainerConfig {
    def static NEO4J_BOLT_PORT = 7687
    def static NEO4J_CONTAINER = new Neo4jContainer(DockerImageName.parse('neo4j').withTag('4.2.4'))
            .withoutAuthentication()
            .withExposedPorts(NEO4J_BOLT_PORT)
            .waitingFor(new LogMessageWaitStrategy().withRegEx('.*Started.*'))

    static {
        NEO4J_CONTAINER.start()
        Runtime.getRuntime().addShutdownHook new Thread({ NEO4J_CONTAINER.stop() })
    }
}
