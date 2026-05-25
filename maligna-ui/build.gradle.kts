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

description = "maligna-ui"

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}
