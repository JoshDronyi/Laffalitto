package com.probrotechsolutions.laffalitto.model.network

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

@OptIn(ExperimentalSerializationApi::class)
open class KtorClient {
    protected val client by lazy {
        HttpClient() {
            install(ContentNegotiation.key.name) {
                Json {
                    isLenient = true
                    explicitNulls = true
                    coerceInputValues = true
                    ignoreUnknownKeys = true
                }
            }
        }
    }

    suspend fun getImage(image: String): ByteArray = withContext(Dispatchers.IO) {
        val images = client.get(image)
        return@withContext images.readBytes()
    }
}