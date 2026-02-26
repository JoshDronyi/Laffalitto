package com.probrotechsolutions.laffalitto.model.network

import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.statement.readBytes
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

open class KtorClient(private val engine: HttpClientEngine? = null) {
    protected val client: HttpClient by lazy {
        if (engine != null) {
            HttpClient(engine) {
                install(ContentNegotiation) {
                    json(Json {
                        isLenient = true
                        explicitNulls = true
                        ignoreUnknownKeys = true
                    })
                }
            }
        } else {
            HttpClient {
                install(ContentNegotiation) {
                    json(Json {
                        isLenient = true
                        explicitNulls = true
                        ignoreUnknownKeys = true
                    })
                }
            }
        }
    }

    suspend fun getImage(image: String): ByteArray = withContext(Dispatchers.IO) {
        val images = client.get(image)
        return@withContext images.readBytes()
    }
}
