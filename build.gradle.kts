import io.wusa.extension.SemverGitPluginExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.3" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
    kotlin("jvm") version "1.4.30" apply false
    kotlin("plugin.spring") version "1.4.30" apply false
    id("com.bmuschko.docker-spring-boot-application") version "6.7.0" apply false
}

subprojects {
    repositories {
        mavenCentral()
    }

    apply {
        plugin("semver")
        plugin("io.spring.dependency-management")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }

    version = (extensions["semver"] as SemverGitPluginExtension).info
}
