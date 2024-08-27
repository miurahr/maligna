plugins {
    application
    `maven-publish`
}

application {
    mainClass = "net.loomchild.maligna.ui.console.Maligna"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":maligna"))
    implementation(libs.commons.cli)
    testImplementation(libs.junit)
    testImplementation(libs.io.takari.junit.takari.cpsuite)
}

group = "net.loomchild"
version = "3.0.2-SNAPSHOT"
description = "maligna-ui"
java.sourceCompatibility = JavaVersion.VERSION_1_8

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}
