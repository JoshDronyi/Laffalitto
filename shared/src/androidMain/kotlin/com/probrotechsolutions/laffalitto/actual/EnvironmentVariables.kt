package com.probrotechsolutions.laffalitto.actual

import com.probrotechsolutions.laffalitto.android.BuildConfig

actual class EnvironmentVariables {
    actual val apiKey: String get() = BuildConfig.APIKEY_JOKE
    actual val host: String get() = BuildConfig.HOST_JOKE
}
