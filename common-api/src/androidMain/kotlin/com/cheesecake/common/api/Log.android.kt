@file:Suppress("unused")

package com.cheesecake.common.api

import android.util.Log

object Log {
    actual fun info(tag: String, message: String) {
        Log.i(tag, message)
    }

    actual fun debug(tag: String, message: String) {
        Log.d(tag, message)
    }

    actual fun error(tag: String, exception: Exception) {
        Log.e(tag, exception.message ?: "Unknown error", exception)
    }
}