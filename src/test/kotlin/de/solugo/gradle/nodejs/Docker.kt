package de.solugo.gradle.nodejs

import org.testcontainers.k3s.K3sContainer
import org.testcontainers.utility.DockerImageName

object Docker {

    val Kubernetes = K3sContainer(DockerImageName.parse("rancher/k3s:v1.21.3-k3s1"))

}