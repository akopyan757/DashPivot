plugins {
    id("java-library")
    id("org.jetbrains.kotlinx.kover") version "0.6.1"
    alias(libs.plugins.kotlinJvm)
}

kotlin {
    jvmToolchain(17)
}

kover {
    filters {
        classes {
            includes += "com.cheesecake.server.auth.route.*" // Укажите свой пакет
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation(libs.java.jwt)
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.time)
    implementation(libs.postgresql)
    implementation(libs.kodein.di)
    implementation(libs.kodein.di.ktor)
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.javax.mail)
    implementation(libs.mindrotJbcrypt)
    implementation(projects.featureAuth.common)
    testImplementation(libs.slf4j.nop)
    testImplementation(libs.mockk)
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.org.jetbrains.kotlin.kotlin.test)
    testImplementation(libs.sqlite.jdbc)
}