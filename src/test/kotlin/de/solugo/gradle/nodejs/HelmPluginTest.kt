package de.solugo.gradle.nodejs

import de.solugo.gradle.test.core.Directory.Helper.extractDirectoryFromClasspath
import de.solugo.gradle.test.core.Directory.Helper.file
import de.solugo.gradle.test.core.Directory.Helper.path
import de.solugo.gradle.test.core.Directory.Helper.withTemporaryDirectory
import de.solugo.gradle.test.core.Executor.Companion.execute
import de.solugo.gradle.test.core.GradleTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class HelmPluginTest {

    @BeforeEach
    fun startDocker() {
        Docker.Kubernetes.apply { if (!isRunning) start() }
    }

    @Test
    fun `install release`() {
        GradleTest {
            withTemporaryDirectory {
                path.extractDirectoryFromClasspath("default")
                path("chart").extractDirectoryFromClasspath("chart")

                file("kube.config").writeText(Docker.Kubernetes.kubeConfigYaml)
            }
            execute("helmInstall") {
                assertThat(output).contains("deployed\ttest-1.0.0")
            }
        }
    }

}