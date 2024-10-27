plugins {
    kotlin("jvm") version "2.0.20"
    application
}

application {
    mainClass = "org.ruud.MainKt"
}

group = "org.ruud"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    testImplementation("org.assertj:assertj-core:3.26.3")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
kotlin {
    jvmToolchain(21)
}
