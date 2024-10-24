@Suppress("DSL_SCOPE_VIOLATION")
plugins {
    id("java-library")
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.serialization)
}

kotlin {
    jvmToolchain(18)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(18))
    }
}

dependencies {
    api(projects.commonApi)
    implementation(libs.kotlinx.serialization.core)
    testImplementation(libs.kotlin.test.junit)
    testImplementation(libs.kotlin.test)
}

