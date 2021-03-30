import io.wusa.GitService.Companion.lastTag
import io.wusa.RegexResolver.Companion.findMatchingRegex
import io.wusa.SemanticVersionFactory
import io.wusa.TagType.LIGHTWEIGHT
import io.wusa.extension.SemverGitPluginExtension
import io.wusa.extension.SemverGitPluginExtension.Companion.DEFAULT_INCREMENTER
import io.wusa.incrementer.VersionIncrementer.Companion.getVersionIncrementerByName
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.4.3" apply false
    id("io.spring.dependency-management") version "1.0.11.RELEASE" apply false
    kotlin("jvm") version "1.4.30" apply false
    kotlin("plugin.spring") version "1.4.30" apply false
    id("com.bmuschko.docker-spring-boot-application") version "6.7.0" apply false
    id("io.wusa.semver-git-plugin") version ("2.3.7")
}

allprojects {

    apply {
        plugin("io.wusa.semver-git-plugin")
    }

    semver {
        snapshotSuffix = ""
        dirtyMarker = ""
        initialVersion = "0.1.0"
        tagType = LIGHTWEIGHT
        branches {
            branch {
                regex = "master"
                incrementer = "CONVENTIONAL_COMMITS_INCREMENTER"
                formatter = Transformer {
                    "${semver.info.version.major}.${semver.info.version.minor}.${semver.info.version.patch}"
                }
            }
            branch {
                regex = ".+"
                incrementer = "NO_VERSION_INCREMENTER"
                formatter = Transformer {
                    "${semver.info.version.major}.${semver.info.version.minor}.${semver.info.version.patch}-${semver.info.branch.name}.build.${semver.info.count}"
                }
            }
        }
    }

    val semver = project.extensions.getByType(SemverGitPluginExtension::class)

    tasks.register("incrementVersion") {
        val semanticVersionFactory = SemanticVersionFactory()
        val tagPrefix = semver.tagPrefix
        val lastTag = lastTag(project, tagPrefix, tagType = semver.tagType)
        val incrementer = findMatchingRegex(
            semver.branches,
            semver.info.branch.name
        )?.let { getVersionIncrementerByName(it.incrementer) } ?: getVersionIncrementerByName(DEFAULT_INCREMENTER)
        incrementer.increment(semanticVersionFactory.createFromString(lastTag.substring(tagPrefix.length)), project)
    }
    project.version = semver.info.toString()
}

subprojects {
    repositories {
        mavenCentral()
    }

    apply {
        plugin("io.spring.dependency-management")
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs = listOf("-Xjsr305=strict")
            jvmTarget = "11"
        }
    }
}
