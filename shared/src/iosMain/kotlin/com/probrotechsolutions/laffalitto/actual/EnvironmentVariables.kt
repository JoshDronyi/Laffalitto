package com.probrotechsolutions.laffalitto.actual

import platform.Foundation.NSBundle

actual class EnvironmentVariables {
    // Production: set RAPIDAPI_KEY and RAPIDAPI_HOST in Info.plist via Xcode xcconfig.
    // The fallback values below are for local development only and should not be shipped.
    actual val apiKey: String get() =
        NSBundle.mainBundle.objectForInfoDictionaryKey("RAPIDAPI_KEY") as? String
            ?: "c6a27b3cd7mshfc50b4cd288dd8ap15319bjsnfc99f7ad8a45"
    actual val host: String get() =
        NSBundle.mainBundle.objectForInfoDictionaryKey("RAPIDAPI_HOST") as? String
            ?: "jokeapi-v2.p.rapidapi.com"
}
