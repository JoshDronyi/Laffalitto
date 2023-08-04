package com.probrotechsolutions.laffalitto

import com.probrotechsolutions.laffalitto.Platform

class DesktopPlatform : Platform {
    override val name: String = "Desktop"
}

actual fun getPlatform(): Platform = DesktopPlatform()