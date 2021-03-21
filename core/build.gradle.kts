plugins {
    kotlin("jvm")
    id("groovy")
}

dependencies {
    val jacksonVersion = "2.12.2"
    implementation("com.fasterxml.jackson.core:jackson-core:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-annotations:$jacksonVersion")
    implementation("com.fasterxml.jackson.core:jackson-databind:$jacksonVersion")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion")

    val spockVersion = "1.3-groovy-2.5"
    testImplementation("org.spockframework:spock-core:$spockVersion")
}
