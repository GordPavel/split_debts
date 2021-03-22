package split.debts.plugin.semver

import io.wusa.extension.SemverGitPluginExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Transformer
import org.gradle.kotlin.dsl.get

class SemverPlugin : Plugin<Project> {
    override fun apply(project: Project): Unit = project.run {
        plugins.apply("io.wusa.semver-git-plugin")
        (extensions["semver"] as SemverGitPluginExtension).also { semver ->
            semver.dirtyMarker = ""
            semver.branches.apply {
                branch {
                    regex = "master"
                    incrementer = "CONVENTIONAL_COMMITS_INCREMENTER"
                    formatter = Transformer {
                        "${semver.info.version.major}.${semver.info.version.minor}.${semver.info.version.patch}"
                    }
                }
                branch {
                    regex = ".+"
                    formatter = Transformer {
                        "${semver.info.version.major}.${semver.info.version.minor}.${semver.info.version.patch}+${semver.info.branch.name}.sha.${semver.info.shortCommit}"
                    }
                }
            }
        }
    }

}