package com.probrotechsolutions.laffalitto.model.network.services

import com.probrotechsolutions.laffalitto.actual.EnvironmentVariables
import com.probrotechsolutions.laffalitto.model.network.KtorClient
import com.probrotechsolutions.laffalitto.model.network.dto.JokeCategoryResponseDTO
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class JokeService(
    private val environmentVariables: EnvironmentVariables
) : KtorClient() {
    companion object {
        private const val BASE_URL = "https://jokeapi-v2.p.rapidapi.com/"
        private const val CATEGORY_ENDPOINT = "categories?format=json"
        private val APIKEY_TAG = "x-rapidApi-key"
        private val HOST_TAG = "x-rapidApi-host"
    }

    private val jokeClient by lazy {
        client
    }

    suspend fun getJokeCategories(): Result<JokeCategoryResponseDTO> {
        val response = jokeClient.get(BASE_URL + CATEGORY_ENDPOINT) {
            headers {
                append(APIKEY_TAG, environmentVariables.apiKey)
                append(HOST_TAG, environmentVariables.host)
                append("contentType", "application/json")
            }
        }
        val successResult = Json.decodeFromString<JokeCategoryResponseDTO>(response.bodyAsText())
        return Result.success(successResult)
    }
}