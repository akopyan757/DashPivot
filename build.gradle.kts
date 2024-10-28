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

gradle.projectsEvaluated {
    allprojects {
        tasks.matching { it.name == "assembleRelease" || it.name == "assembleDebug" || it.name == "package" }.configureEach {
            dependsOn(":prepareVersioning")
        }
    }
}