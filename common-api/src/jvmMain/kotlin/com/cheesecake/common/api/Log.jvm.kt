@file:Suppress("unused")

package com.cheesecake.common.api

actual object Log {
    actual fun info(tag: String, message: String) {
        println("INFO: [$tag] $message")
    }

    actual fun debug(tag: String, message: String) {
        println("DEBUG: [$tag] $message")
    }

    actual fun error(tag: String, exception: Exception) {
        println("ERROR: [$tag] ${exception.message}")
    }
}