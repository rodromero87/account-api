plugins {
    kotlin("jvm") version "2.2.21"
    kotlin("plugin.spring") version "2.2.21"
    id("org.springframework.boot") version "4.0.2"
    id("io.spring.dependency-management") version "1.1.7"
}

group = "com.account"
version = "0.0.1-SNAPSHOT"
description = "SimulatorTransaction - load generator for SQS transactions"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
}

dependencies {
    // Web + Actuator
    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:3.0.1")


    // ✅ Validation (para DTOs com @Valid, @Min, @Max)
    implementation("org.springframework.boot:spring-boot-starter-validation")

    // Kotlin / Jackson
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // ✅ corrigido (era tools.jackson.module)
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // ✅ Coroutines (para paralelismo e throttle)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.9.0")

    // ✅ AWS SDK v2 - SQS (async client + batch)
    implementation("software.amazon.awssdk:sqs:2.25.62")

    // (Opcional, mas recomendado): Netty async client (melhora throughput)
    implementation("software.amazon.awssdk:netty-nio-client:2.25.62")

    // Tests
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict", "-Xannotation-default-target=param-property")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
