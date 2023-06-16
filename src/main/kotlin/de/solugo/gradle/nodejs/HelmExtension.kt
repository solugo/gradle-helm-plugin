package de.solugo.gradle.nodejs

import org.gradle.api.Project
import org.gradle.process.ExecSpec
import org.yaml.snakeyaml.Yaml
import java.io.File

class HelmExtension(private val project: Project) {
    private val yaml = Yaml()

    val version = project.objects.property(String::class.java).convention(
        project.provider { "3.12.1" }
    )
    val cachePath = project.objects.property(File::class.java).convention(
        project.provider { project.file(System.getProperty("user.home")).resolve(".gradle/helm") }
    )
    val kubeConfig = project.objects.property(File::class.java)
    val kubeContext = project.objects.property(String::class.java)
    val kubeNamespace = project.objects.property(String::class.java)

    fun version(value: Any?) {
        version.set(value?.toString())
    }

    fun cachePath(value: Any?) {
        cachePath.set(value?.let { project.file(it) })
    }

    fun kubeConfig(value: Any?) {
        kubeConfig.set(value?.let { project.file(it) })
    }

    fun kubeContext(value: String?) {
        kubeContext.set(value)
    }

    fun kubeNamespace(value: String?) {
        kubeNamespace.set(value)
    }

    val instance
        get() = HelmRegistry.resolve(
            version = version.get(),
            cacheFolder = cachePath.get(),
            onInstall = { version, folder ->
                project.logger.lifecycle("Installing Helm v$version to $folder")
            }
        )

    fun valuesFile(values: Map<String, Any?>) = File.createTempFile("helm-values-", ".yml").apply {
        writeText(yaml.dump(values))
    }

    fun exec(vararg args: String) = exec {
        commandLine(*args)
    }

    fun exec(action: ExecSpec.() -> Unit) = project.exec { spec ->
        spec.action()
        spec.commandLine(
            buildList {
                add(instance.binary.toString())
                kubeConfig.orNull?.let { project.file(it) }?.also {
                    add("--kubeconfig")
                    add(it.absolutePath)
                }
                kubeContext.orNull?.also {
                    add("--kube-context")
                    add(it)
                }
                kubeNamespace.orNull?.also {
                    add("--namespace")
                    add(it)
                }
                addAll(spec.commandLine)
            }
        )
    }

}