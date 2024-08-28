plugins {
    application
    java
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
    implementation(libs.junit)
    implementation(libs.io.takari.junit.takari.cpsuite)
}

group = "net.loomchild"
version = "4.0.0"
description = "maligna-ui"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}
