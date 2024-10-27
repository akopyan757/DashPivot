package com.cheesecake.common.api

import java.io.File

actual fun readVersionFile(): String? {
    return try {
        File("../VERSION").readText().trim()
    } catch (e: Exception) {
        null
    }
}