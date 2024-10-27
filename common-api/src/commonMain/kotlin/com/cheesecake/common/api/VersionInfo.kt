package com.cheesecake.common.api

val version: String by lazy {
    readVersionFile() ?: "unknown"
}

expect fun readVersionFile(): String?