package com.cheesecake.common.api

import android.os.Build.VERSION
import android.util.Log

// androidMain/src/androidMain/kotlin/VersionUtils.kt
actual fun readVersionFile(): String? {
    Log.i("VersionInfo", "android: readVersionFile: Start")
    return try {
        val url = VERSION::class.java.classLoader?.getResource("VERSION")
        Log.i("VersionInfo", "android: inputStream: $url")
        url?.readText()?.trim()
    } catch (e: Exception) {
        Log.e("VersionInfo", "android: readVersionFile", e)
        null
    }
}