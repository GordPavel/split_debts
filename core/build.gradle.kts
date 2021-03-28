import split.debts.plugin.versions.SPOCK_VERSION
import split.debts.plugin.versions.JACKSON_VERSION

plugins {
    kotlin("jvm")
    id("groovy")
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-core:${JACKSON_VERSION}")
    implementation("com.fasterxml.jackson.core:jackson-annotations:${JACKSON_VERSION}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${JACKSON_VERSION}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${JACKSON_VERSION}")

    testImplementation("org.spockframework:spock-core:${SPOCK_VERSION}")
}
