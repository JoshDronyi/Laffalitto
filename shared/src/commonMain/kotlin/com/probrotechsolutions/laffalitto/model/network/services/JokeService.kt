package com.probrotechsolutions.laffalitto.model.network.services

import com.probrotechsolutions.laffalitto.actual.EnvironmentVariablesContract
import com.probrotechsolutions.laffalitto.model.network.KtorClient
import com.probrotechsolutions.laffalitto.model.network.dto.JokeCategoryResponseDTO
import com.probrotechsolutions.laffalitto.model.network.dto.JokeResponseDTO
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import io.ktor.http.isSuccess
import kotlinx.serialization.json.Json

class JokeService(
    private val environmentVariables: EnvironmentVariables,
    engine: HttpClientEngine? = null
) : KtorClient(engine), JokeServiceInterface {
    companion object {
        private const val BASE_URL = "https://jokeapi-v2.p.rapidapi.com/"
        private const val CATEGORY_ENDPOINT = "categories?format=json"
        private const val JOKE_ENDPOINT = "joke/"
        private const val APIKEY_TAG = "X-RapidAPI-Key"
        private const val HOST_TAG = "X-RapidAPI-Host"
    }

    private val jokeClient by lazy { client }

    override suspend fun getJokeCategories(): Result<JokeCategoryResponseDTO> = try {
        val response = jokeClient.get(BASE_URL + CATEGORY_ENDPOINT) {
            headers {
                append(APIKEY_TAG, environmentVariables.apiKey)
                append(HOST_TAG, environmentVariables.host)
                append("Content-Type", "application/json")
            }
        }
        if (!response.status.isSuccess()) {
            Result.failure(Throwable("HTTP ${response.status.value}: ${response.status.description}"))
        } else {
            val dto = Json.decodeFromString<JokeCategoryResponseDTO>(response.bodyAsText())
            Result.success(dto)
        }
    } catch (ex: Exception) {
        Result.failure(ex)
    }

    override suspend fun getJoke(category: String): Result<JokeResponseDTO> = try {
        val response = jokeClient.get(BASE_URL + JOKE_ENDPOINT + category) {
            headers {
                append(APIKEY_TAG, environmentVariables.apiKey)
                append(HOST_TAG, environmentVariables.host)
                append("Content-Type", "application/json")
            }
        }
        if (!response.status.isSuccess()) {
            Result.failure(Throwable("HTTP ${response.status.value}: ${response.status.description}"))
        } else {
            val dto = Json.decodeFromString<JokeResponseDTO>(response.bodyAsText())
            Result.success(dto)
        }
    } catch (ex: Exception) {
        Result.failure(ex)
    }
}
