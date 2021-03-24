import split.debts.plugin.versions.DependenciesVersionsPlugin.JACKSON_VERSION
import split.debts.plugin.versions.DependenciesVersionsPlugin.SPOCK_VERSION

plugins {
    kotlin("jvm")
    id("groovy")
}

dependencies {
    implementation("com.fasterxml.jackson.core:jackson-core:${JACKSON_VERSION.version}")
    implementation("com.fasterxml.jackson.core:jackson-annotations:${JACKSON_VERSION.version}")
    implementation("com.fasterxml.jackson.core:jackson-databind:${JACKSON_VERSION.version}")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:${JACKSON_VERSION.version}")

    testImplementation("org.spockframework:spock-core:${SPOCK_VERSION.version}")
}
