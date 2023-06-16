plugins {
    id("java")
    id("de.solugo.helm")
}

helm {
    kubeConfig("./kube.config")
    kubeNamespace("test-namespace")
}

tasks.create("helmInstall") {
    doFirst {
        val valuesFile = helm.valuesFile(
            mapOf(
                "name" to "test-install"
            )
        )

        logger.lifecycle(valuesFile.absolutePath)
        helm.exec("install", "-f", valuesFile.absolutePath, "--create-namespace", "test", "./chart")
        helm.exec("list")
    }
}

tasks.create("helmList") {
    doFirst {
        helm.exec("list")
    }
}