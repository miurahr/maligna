plugins {
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

val sonatypeUsername: String? by project
val sonatypePassword: String? by project

nexusPublishing.repositories {
    sonatype {
        // stagingProfileId = "121f28671d24dc"
        if (sonatypeUsername != null && sonatypePassword != null) {
            username.set(sonatypeUsername)
            password.set(sonatypePassword)
        } else {
            username.set(System.getenv("SONATYPE_USER"))
            password.set(System.getenv("SONATYPE_PASS"))
        }
    }
}
