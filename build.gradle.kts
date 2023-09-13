
val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project
val exposed_version: String by project
val h2_version: String by project
val koin_version: String by project
val hikaricp_version: String by project
val ehcache_version: String by project
val koin_ktor_version: String by project
val koin_ksp_version: String by project
val postgresql_version: String by project

plugins {
    application
    kotlin("jvm") version "1.9.10"
    id("io.ktor.plugin") version "2.3.4"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.10"
    // KSP for Koin Annotations
    id("com.google.devtools.ksp") version "1.9.10-1.0.13"
}

group = "com.des"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-host-common-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-cors")
    implementation("ch.qos.logback:logback-classic:$logback_version")

    // Koin for Dependency Injection
    implementation("io.insert-koin:koin-ktor:$koin_ktor_version") // Koin for Ktor
    implementation("io.insert-koin:koin-logger-slf4j:$koin_ktor_version") // Koin Logger
    implementation("io.insert-koin:koin-annotations:$koin_ksp_version") // Koin Annotations for KSP
    ksp("io.insert-koin:koin-ksp-compiler:$koin_ksp_version") // Koin KSP Compiler for KSP


    // DB
    implementation("org.jetbrains.exposed:exposed-core:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-dao:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposed_version")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposed_version")
    implementation("com.h2database:h2:$h2_version")
    implementation("com.zaxxer:HikariCP:$hikaricp_version")
    implementation("org.ehcache:ehcache:$ehcache_version")
    implementation("org.postgresql:postgresql:$postgresql_version")

    // Koin for Ktor
    implementation("io.insert-koin:koin-ktor:$koin_version")
    // SLF4J Logger
    implementation("io.insert-koin:koin-logger-slf4j:$koin_version")

    implementation("io.ktor:ktor-server-swagger:$ktor_version")

    testImplementation("io.ktor:ktor-server-tests-jvm")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")
    testImplementation("io.ktor:ktor-client-content-negotiation:$ktor_version")

    // https://ktor.io/docs/testing.html
    testImplementation("io.ktor:ktor-server-test-host:$ktor_version")
    testImplementation("org.jetbrains.kotlin:kotlin-test:$kotlin_version")

    // Koin testing tools
    testImplementation("io.insert-koin:koin-test:$koin_version")
    // Needed JUnit version
    testImplementation("io.insert-koin:koin-test-junit4:$koin_version")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
}
