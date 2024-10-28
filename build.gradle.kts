plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.kotlinJvm) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    alias(libs.plugins.kotlin.android) apply false
}


tasks.register("buildAndRunDockerCompose") {
    group = "Docker"
    description = "Builds the project and runs Docker Compose"

    doLast {
        // Сначала выполняем сборку сервера
        exec {
            commandLine("sh", "-c", "./gradlew server:build")
        }
        exec {
            commandLine("sh", "-c", "docker-compose up --build")
        }
    }
}

tasks.register("stage") {
    dependsOn(":server:build")
}

val readVersion by tasks.registering {
    group = "versioning"
    description = "Reads the project version from the VERSION file."

    doLast {
        val versionFile = file("VERSION")
        val version = versionFile.readText().trim()
        println("Current project version: $version")

        // Сохраняем версию в проектных свойствах, чтобы другие задачи могли к ней обращаться
        project.extra["projectVersion"] = version
    }
}

val updateVersionInfo by tasks.registering {
    group = "versioning"
    description = "Updates VersionInfo.kt with the project version."

    dependsOn(readVersion) // Обеспечиваем, что версия будет прочитана перед выполнением

    doLast {
        val version = project.extra["projectVersion"] as String
        val versionInfoFile = file("common-api/src/commonMain/kotlin/com/cheesecake/common/api/VersionInfo.kt")

        versionInfoFile.writeText("""
            package com.cheesecake.common.api

            object VersionInfo {
                const val PROJECT_VERSION = "$version"
            }
        """.trimIndent())

        println("VersionInfo.kt updated with version: $version")
    }
}

val createGitTag by tasks.registering {
    group = "versioning"
    description = "Creates a Git tag for the current project version."

    dependsOn(readVersion) // Обеспечиваем, что версия будет прочитана перед выполнением

    doLast {
        val version = project.extra["projectVersion"] as String

        // Проверяем, существует ли тег, чтобы не создавать дубликаты
        val existingTags = "git tag --list".runCommand()
        if (existingTags != null && !existingTags.contains(version)) {
            "git tag $version".runCommand()
            println("Created Git tag: $version")
        } else if (existingTags != null) {
            println("Git tag $version already exists.")
        } else {
            println("Failed to retrieve existing tags.")
        }
    }
}

val prepareVersioning by tasks.registering {
    group = "versioning"
    description = "Reads version, updates VersionInfo.kt, and creates a Git tag."

    dependsOn(readVersion, updateVersionInfo, createGitTag)
}

allprojects {
    afterEvaluate {
        tasks.matching {
            it.name == "build" || it.name == "assemble" || it.name == "package" || it.name.contains("assembleRelease")
        }.configureEach {
            dependsOn(prepareVersioning)
        }
    }
}

fun String.runCommand(): String? {
    return try {
        val process = ProcessBuilder(*this.split(" ").toTypedArray())
            .redirectOutput(ProcessBuilder.Redirect.PIPE)
            .redirectError(ProcessBuilder.Redirect.PIPE)
            .start()
        process.inputStream.bufferedReader().readText().trim()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}