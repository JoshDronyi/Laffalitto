package com.probrotechsolutions.laffalitto.actual

actual class EnvironmentVariables {
    private val props = java.util.Properties().also { p ->
        java.io.File("local.properties").takeIf { it.exists() }
            ?.inputStream()?.use { p.load(it) }
    }
    actual val apiKey: String get() = props.getProperty("apiKey.joke", "")
    actual val host: String get() = props.getProperty("host.joke", "")
}
