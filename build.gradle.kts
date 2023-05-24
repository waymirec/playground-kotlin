import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.20"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.0-RC")
    implementation("commons-codec:commons-codec:1.15")

    testImplementation("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.9.2")
    testImplementation("org.assertj:assertj-core:3.24.2")
    testImplementation("org.hamcrest:hamcrest:2.2")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
    minHeapSize = "1024m"
    maxHeapSize = "4096m"
    jvmArgs = listOf("-Xms1g", "-Xmx4g")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}