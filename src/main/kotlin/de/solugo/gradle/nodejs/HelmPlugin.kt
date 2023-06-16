package de.solugo.gradle.nodejs

import org.gradle.api.Plugin
import org.gradle.api.Project

class HelmPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = HelmExtension(project)
        project.extensions.add("helm", extension)
    }
}