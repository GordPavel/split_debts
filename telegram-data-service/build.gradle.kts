plugins {
    kotlin("jvm")
}

dependencies {
    implementation(project(":core"))
    implementation("io.projectreactor:reactor-core:3.4.3")
}
