import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
}

group = "me.martelli"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // todo
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "18"
}
