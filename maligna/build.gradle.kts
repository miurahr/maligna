plugins {
    `java-library`
    `maven-publish`
}

repositories {
    mavenCentral()
}

val jaxb: Configuration by configurations.creating

dependencies {
    implementation(libs.net.loomchild.segment)
    implementation(libs.jaxb.api)
    implementation(libs.jaxb.core)
    implementation(libs.commons.logging)
    runtimeOnly(libs.jaxb.runtime)
    testImplementation(libs.junit)
    testImplementation(libs.io.takari.junit.takari.cpsuite)
    testImplementation(libs.jaxb.api)
    testImplementation(libs.jaxb.core)
    testRuntimeOnly(libs.jaxb.runtime)
    jaxb(libs.jaxb.xjc)
}

group = "net.loomchild"
version = "3.0.2-SNAPSHOT"
description = "maligna"
java.sourceCompatibility = JavaVersion.VERSION_1_8

java {
    withSourcesJar()
    withJavadocJar()
}

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    setFailOnError(false)
    options {
        encoding = "UTF-8"
        jFlags("-Duser.language=en")
    }
}

val xjcOutputDir = layout.buildDirectory.dir("generated/xjc/main").get().asFile.path
val createXjcOutputDir by tasks.register("createXjcOutputDir") {
    doLast {
        mkdir(xjcOutputDir)
    }
}

val schemaDir = "src/main/resources/net/loomchild/maligna/res/xml"
val xjcPackage = "net.loomchild.maligna.util.bind"

val xjcAl by tasks.registering(JavaExec::class) {
    // Directory needs to exist
    dependsOn(createXjcOutputDir)
    classpath = jaxb
    mainClass.set("com.sun.tools.xjc.XJCFacade")
    args = listOf("-no-header", "-d", xjcOutputDir, "-p", xjcPackage + ".al", "-encoding", "UTF-8", "-quiet",
        schemaDir + "/al.xsd"
    )
}
tasks.getByName("compileJava").dependsOn(xjcAl)

val xjcTmx by tasks.registering(JavaExec::class) {
    // Directory needs to exist
    dependsOn(createXjcOutputDir)
    classpath = jaxb
    mainClass.set("com.sun.tools.xjc.XJCFacade")
    args = listOf("-no-header", "-d", xjcOutputDir, "-p", xjcPackage + ".tmx", "-encoding", "UTF-8", "-quiet",
        schemaDir + "/tmx.xsd"
    )
}
tasks.getByName("compileJava").dependsOn(xjcTmx)

sourceSets {
    main {
        java {
            srcDir(listOf(layout.buildDirectory.dir("generated/xjc/main"), "src/main/java"))
        }
    }
}

tasks.withType(Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
