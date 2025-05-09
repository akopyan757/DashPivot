@file:Suppress("unused")

package com.cheesecake.common.api

import platform.Foundation.NSLog

object Log {
    actual fun info(tag: String, message: String) {
        NSLog("INFO: [$tag] $message")
    }

    actual fun debug(tag: String, message: String) {
        NSLog("DEBUG: [$tag] $message")
    }

    actual fun error(tag: String, exception: Exception) {
        NSLog("ERROR: [$tag] ${exception.message}")
    }
}