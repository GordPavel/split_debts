import org.springframework.boot.gradle.tasks.bundling.BootBuildImage
import org.springframework.boot.gradle.tasks.bundling.BootJar
import split.debts.plugin.versions.DependenciesVersionsPlugin.*

plugins {
    id("org.springframework.boot")
    kotlin("jvm")
    kotlin("plugin.spring")
    id("groovy")
    kotlin("kapt")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework.data:spring-data-neo4j")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation("org.mapstruct:mapstruct:${MAPSTRUCT_VERSION.version}")
    kapt("org.mapstruct:mapstruct-processor:${MAPSTRUCT_VERSION.version}")

    implementation(project(":core"))
    implementation(project(":telegram-data-service"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.spockframework:spock-core:${SPOCK_VERSION.version}")
    testImplementation("org.spockframework:spock-spring:${SPOCK_VERSION.version}")
    testImplementation("org.testcontainers:junit-jupiter")
    testImplementation("org.testcontainers:neo4j")
    testImplementation("org.assertj:assertj-core:3.4.1")
}

tasks.getByName<BootJar>("bootJar") { enabled = false }
tasks.getByName<BootBuildImage>("bootBuildImage") { enabled = false }
tasks.getByName<Jar>("jar") { enabled = true }

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${SPRING_CLOUD_VERSION.version}")
        mavenBom("org.testcontainers:testcontainers-bom:${TESTCONTAINERS_VERSION.version}")
    }
}
