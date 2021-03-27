import split.debts.plugin.versions.DependenciesVersionsPlugin.*

plugins {
    id("org.springframework.boot")
    kotlin("jvm")
    kotlin("plugin.spring")
    id("groovy")
}

dependencies {
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    annotationProcessor("org.projectlombok:lombok")

    implementation("org.springframework.boot:spring-boot-starter-web")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    implementation("org.telegram:telegrambots-spring-boot-starter:4.1")
    implementation(project(":core"))
    implementation(project(":telegram-data-service"))
    implementation(project(":telegram-neo4j-data-service"))

    implementation("io.github.microutils:kotlin-logging:1.12.5")
    implementation(project(":telegram-bot-sleuth-tracing-starter"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.spockframework:spock-core:${SPOCK_VERSION.version}")
    testImplementation("org.spockframework:spock-spring:${SPOCK_VERSION.version}")
    testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock")
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${TESTCONTAINERS_VERSION.version}")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${SPRING_CLOUD_VERSION.version}")
    }
}
