import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    
    jvm()
    jvmToolchain(17)
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
        }
        jsMain.dependencies {
            dependencies {
                implementation(libs.web.core)
                implementation(libs.runtime)
            }
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodel)
            implementation(libs.androidx.lifecycle.runtime.compose)
            implementation(projects.featureAuth.data)
            implementation(projects.featureAuth.ui)
            implementation(libs.koin.core)
        }
        commonTest.dependencies {

        }
        jvmMain.dependencies {
            //implementation(libs.skiko.jvm.runtime.macos.arm64)
            //implementation(libs.skiko.jvm.runtime.macos.x64)
            implementation(libs.koin.jvm)
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

android {
    namespace = "com.cheesecake.dashpivot"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.cheesecake.dashpivot"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = (project.findProperty("projectVersion") as? String) ?: "1.0"

        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
        applicationVariants.all { variant ->
            variant.outputs.all { output ->
                val outputImpl = output as com.android.build.gradle.internal.api.BaseVariantOutputImpl
                outputImpl.outputFileName = "dashpivot-${variant.name}.apk"
                true
            }
        }
    }
    applicationVariants.forEach { variant ->
        variant.outputs.forEach { output ->
            val version = project.extra["projectVersion"] as? String ?: "1.0"
            val outputImpl = output as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            outputImpl.outputFileName = "dashpivot-${variant.name}-$version.apk"
        }
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/NOTICE.md"
        }
    }
    signingConfigs {
        create("release") {
            storeFile = file("dash-pivot-keystore.jks")
            storePassword = "zaq12345"
            keyAlias = "dash-pivot-key"
            keyPassword = "zaq12345"
        }
    }
    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
        }
    }
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {
    application {
        mainClass = "com.cheesecake.dashpivot.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.cheesecake.dashpivot"
            packageVersion = "1.0.0"
        }
    }
}