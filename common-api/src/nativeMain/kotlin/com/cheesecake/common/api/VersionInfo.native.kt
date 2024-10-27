package com.cheesecake.common.api

import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSBundle
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.stringWithContentsOfFile

@OptIn(ExperimentalForeignApi::class)
actual fun readVersionFile(): String? {
    val path = NSBundle.mainBundle.pathForResource("VERSION", null)
    return path?.let {
        NSString.stringWithContentsOfFile(it, NSUTF8StringEncoding, null)
    }?.trim()
}
