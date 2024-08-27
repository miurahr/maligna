plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
}

sourceSets.create("gen")

val jaxb = configurations.create("jaxb")

dependencies {
    implementation(libs.net.loomchild.segment)
    implementation(libs.jaxb.api)
    implementation(libs.jaxb.core)
    implementation(libs.jaxb.impl)
    implementation(libs.commons.logging)
    testImplementation(libs.junit)
    testImplementation(libs.io.takari.junit.takari.cpsuite)
    jaxb(libs.jaxb.api)
}

group = "net.loomchild"
version = "3.0.2-SNAPSHOT"
description = "maligna"
java.sourceCompatibility = JavaVersion.VERSION_1_8

val testsJar by tasks.registering(Jar::class) {
    archiveClassifier = "tests"
    from(sourceSets["test"].output)
}

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
        artifact(testsJar)
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}

tasks.create<JavaExec>("alJaxb") {
    setClasspath(jaxb)
    mainClass.set("com.sun.tools.xjc.XJCFacade")
    args = listOf("-no-header", "-d", "src", "-p", "net.loomchild.maligna.bind.al", "src/main/resources/net/loomchild/maligna/res/xml/al.xsd")
    outputs.dir("src/gen/java/net/loomchild/maligna/bind/al")
}
