[![License](https://img.shields.io/github/license/solugo/gradle-nodejs-plugin.svg?style=for-the-badge)](https://github.com/solugo/gradle-nodejs-plugin/blob/master/LICENSE)
[![Version](https://img.shields.io/maven-metadata/v/https/plugins.gradle.org/m2/de/solugo/gradle/gradle-nodejs-plugin/maven-metadata.xml.svg?style=for-the-badge)](https://plugins.gradle.org/m2/de/solugo/gradle/gradle-nodejs-plugin/)

# [Gradle Helm Plugin](https://plugins.gradle.org/plugin/de.solugo.helm)
This plugin allows to use [Helm](https://helm.sh/) tools via gradle. Helm will be downloaded automatically and reused across 
execution.

## Configuration
<pre>
plugins {
    id("de.solugo.helm") version "..."
}

nodejs {
    version.set(...) // default: "18.16.0"
    cachePath.set(...) // default: "~/.gradle/helm"
    kubeConfig.set(...) // default: null 
    kubeContext.set(...) // default: null 
    kubeNamespace.set(...) // default: null 
}
</pre>

## Examples

### Install helm chart in folder chart
```kotlin
helm {
    kubeConfig("./kube.config")
    kubeNamespace("test-namespace")
}

tasks.create("buildFrontend") {
    doFirst {
        val valuesFile = helm.valuesFile(
            mapOf(
                "custom" to "value"
            )
        )

        helm.exec("install", "-f", valuesFile.absolutePath, "test", "./chart")
    }
}
```