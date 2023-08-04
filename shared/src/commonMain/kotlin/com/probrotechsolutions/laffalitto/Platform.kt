package com.probrotechsolutions.laffalitto

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform