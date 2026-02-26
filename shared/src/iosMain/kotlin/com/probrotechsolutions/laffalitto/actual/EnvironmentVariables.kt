package com.probrotechsolutions.laffalitto.actual

actual class EnvironmentVariables {
    // TODO: For production, inject via Xcode xcconfig + Info.plist rather than hardcoding here.
    actual val apiKey: String get() = "c6a27b3cd7mshfc50b4cd288dd8ap15319bjsnfc99f7ad8a45"
    actual val host: String get() = "jokeapi-v2.p.rapidapi.com"
}
