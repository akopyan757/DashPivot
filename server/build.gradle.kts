plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

group = "com.cheesecake.dashpivot"
version = "1.0.0"
application {
    mainClass.set("com.cheesecake.dashpivot.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(18))
    }
}

dependencies {
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.postgresql)
    implementation(libs.kodein.di)
    implementation(libs.kodein.di.ktor)
    implementation(projects.server.auth)
    implementation(projects.commonAuth)
    implementation(libs.logback)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.json)
    implementation(project(":shared"))
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)
}