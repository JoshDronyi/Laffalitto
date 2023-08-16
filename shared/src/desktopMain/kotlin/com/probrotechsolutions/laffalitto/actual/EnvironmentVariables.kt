package com.probrotechsolutions.laffalitto.actual

actual class EnvironmentVariables {
    actual val apiKey: String
        get() = "c6a27b3cd7mshfc50b4cd288dd8ap15319bjsnfc99f7ad8a45" // BUildConfig.APIKEY_JOKE
    actual val host: String
        get() = "jokeapi-v2.p.rapidapi.com"//BuildConfig.HOST_JOKE
}