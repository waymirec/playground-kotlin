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

    testImplementation("org.junit.jupiter:junit-jupiter-params:5.1.0")
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