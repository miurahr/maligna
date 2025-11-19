plugins {
    alias(libs.plugins.nexus.publish)
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.BIN
    gradleVersion = "8.14.2"
}

val sonatypeUsername: String? by project
val sonatypePassword: String? by project

nexusPublishing.repositories {
    sonatype {
        stagingProfileId = "121f28671d24dc"
        if (sonatypeUsername != null && sonatypePassword != null) {
            username.set(sonatypeUsername)
            password.set(sonatypePassword)
        } else {
            username.set(System.getenv("SONATYPE_USER"))
            password.set(System.getenv("SONATYPE_PASS"))
        }
    }
}
