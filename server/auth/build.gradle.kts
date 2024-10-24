plugins {
    id("java-library")
    alias(libs.plugins.kotlinJvm)
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
    implementation(projects.commonAuth)
}