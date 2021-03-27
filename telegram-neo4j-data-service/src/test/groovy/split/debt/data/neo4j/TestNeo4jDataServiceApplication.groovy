package split.debt.data.neo4j

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.neo4j.Neo4jReactiveRepositoriesAutoConfiguration

@SpringBootApplication(
        exclude = [
                Neo4jReactiveRepositoriesAutoConfiguration,
        ]
)
class TestNeo4jDataServiceApplication {
    static void main(String[] args) {
        SpringApplication.run(TestNeo4jDataServiceApplication, args)
    }
}
