plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

group = "com.cheesecake.dashpivot"
version = "0.1.0"
application {
    mainClass.set("com.cheesecake.dashpivot.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${extra["io.ktor.development"] ?: "false"}")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
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
    testImplementation(libs.ktor.server.tests)
    testImplementation(libs.kotlin.test.junit)
}

tasks.register("jarWithVersion", Jar::class) {
    archiveFileName.set("server-${project.version}.jar")
    from(sourceSets.main.get().output)
    dependsOn("build")
}