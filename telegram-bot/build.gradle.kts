import split.debts.plugin.versions.DependenciesVersionsPlugin.*

plugins {
    id("org.springframework.boot")
    kotlin("jvm")
    kotlin("plugin.spring")
    id("groovy")
    id("com.bmuschko.docker-spring-boot-application")
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
    implementation("org.springframework.cloud:spring-cloud-sleuth-zipkin")
    implementation("org.springframework.cloud:spring-cloud-starter-sleuth")
    runtimeOnly("io.micrometer:micrometer-registry-prometheus")

    implementation("org.telegram:telegrambots-spring-boot-starter:4.1")
    implementation(project(":core"))

    implementation("io.github.microutils:kotlin-logging:1.12.5")
    implementation(project(":telegram-bot-sleuth-tracing-starter"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.spockframework:spock-core:${SPOCK_VERSION.version}")
    testImplementation("org.spockframework:spock-spring:${SPOCK_VERSION.version}")
    testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock")
}

val dockerImageName = "gordeevp/split-debts-telegram-bot"

tasks.register<DockerTagImage>("dockerTagImage") {
    dependsOn("dockerBuildImage")
    group = "docker"
    targetImageId("split_debts/telegram-bot:${project.version}")
    repository.set(dockerImageName)
    tag.set(project.version.toString())
}

tasks.withType<DockerPushImage> {
    dependsOn("dockerTagImage")
    group = "docker"
    images.set(setOf(dockerImageName))
}

dependencyManagement {
    imports {
        mavenBom("org.testcontainers:testcontainers-bom:${TESTCONTAINERS_VERSION.version}")
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${SPRING_CLOUD_VERSION.version}")
    }
}
