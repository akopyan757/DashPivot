package com.cheesecake.dashpivot

class DesktopPlatform : Platform {
    override val name: String = "Desktop"
}

actual fun getPlatform(): Platform {
   return DesktopPlatform()
}