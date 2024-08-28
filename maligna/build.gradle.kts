import com.github.spotbugs.snom.Confidence

plugins {
    `java-library`
    `maven-publish`
    signing
    alias(libs.plugins.spotbugs)
}

repositories {
    mavenCentral()
}

val jaxb: Configuration by configurations.creating

dependencies {
    implementation(libs.net.loomchild.segment)
    implementation(libs.jaxb4.api)
    implementation(libs.jaxb4.core)
    runtimeOnly(libs.jaxb4.runtime)
    testImplementation(libs.junit)
    testImplementation(libs.io.takari.junit.takari.cpsuite)
    testRuntimeOnly(libs.jaxb4.runtime)
    jaxb(libs.jaxb4.xjc)
    jaxb(libs.jaxb4.api)
    jaxb(libs.jaxb4.runtime)
}

group = "tokyo.northside"
version = "4.0.0-SNAPSHOT"
description = "maligna"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
    withSourcesJar()
    withJavadocJar()
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

tasks.jar {
    manifest {
        attributes("Automatic-Module-Name" to "net.loomchild.mALIGNa")
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            setGroupId("tokyo.northside")
            pom {
                name = "maligna"
                description = "maligna"
                url.set("https://github.com/miurahr/maligna")
                licenses {
                    license {
                        name.set("The MIT License(MIT)")
                        url.set("https://opensource.org/license/MIT")
                    }
                }
                developers {
                    developer {
                        id.set("miurahr")
                        name.set("Hiroshi Miura")
                        email.set("miurahr@linux.com")
                    }
                }
                scm {
                    connection.set("scm:git:git://github.com/miurahr/maligna.git")
                    developerConnection.set("scm:git:git://github.com/miurahr/maligna.git")
                    url.set("https://github.com/miurahr/maligna")
                }
            }
        }
    }
}

spotbugs {
    reportLevel = Confidence.valueOf("HIGH")
    tasks.spotbugsMain {
        reports.create("html") {
            required.set(true)
        }
    }
    tasks.spotbugsTest {
        reports.create("html") {
            required.set(true)
        }
    }
}
