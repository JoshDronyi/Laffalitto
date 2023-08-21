package com.probrotechsolutions.laffalitto.model.network.services

import com.probrotechsolutions.laffalitto.actual.EnvironmentVariables
import com.probrotechsolutions.laffalitto.model.local.flags.Flag
import com.probrotechsolutions.laffalitto.model.network.KtorClient
import com.probrotechsolutions.laffalitto.model.network.dto.flagResponse.FlagListResponseDTO
import com.probrotechsolutions.laffalitto.model.network.dto.jokeCategoryResponse.JokeCategoryResponseDTO
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import co.touchlab.kermit.Logger
import com.probrotechsolutions.laffalitto.model.network.dto.JokeResponse.JokeResponseDTO

class JokeService(
    private val environmentVariables: EnvironmentVariables
) : KtorClient() {
    companion object {
        private const val BASE_URL = "https://jokeapi-v2.p.rapidapi.com/"
        private const val formatOption = "format=json"
        private const val CATEGORY_ENDPOINT = "categories?$formatOption"
        private const val FLAGS_ENDPOINT = "flags?$formatOption"
        private const val JOKE_ENDPOINT = "joke"
        private const val APIKEY_TAG = "x-rapidApi-key"
        private const val HOST_TAG = "x-rapidApi-host"
        private const val JOKE_API_URL_SEPARATOR = "%2C"
    }

    private val jokeClient by lazy { client }

    private fun HttpRequestBuilder.addJokeApiHeaders() {
        headers {
            append(APIKEY_TAG, environmentVariables.apiKey)
            append(HOST_TAG, environmentVariables.host)
        }
    }

    suspend fun getJokeCategories(): Result<JokeCategoryResponseDTO> {
        val response = jokeClient.get(BASE_URL + CATEGORY_ENDPOINT) {
            addJokeApiHeaders()
        }
        val successResult = Json.decodeFromString<JokeCategoryResponseDTO>(response.bodyAsText())
        return Result.success(successResult)
    }

    suspend fun getFlags(): Result<FlagListResponseDTO> {
        val response = jokeClient.get(BASE_URL + FLAGS_ENDPOINT) {
            addJokeApiHeaders()
        }
        val successResult = Json.decodeFromString<FlagListResponseDTO>(response.bodyAsText())
        return Result.success(successResult)
    }

    suspend fun getJoke(category: String, vararg flag: Flag): Result<JokeResponseDTO> {
        var jokeUrl = buildString {
            append("/$category")
            append("?$formatOption")
            if (flag.isNotEmpty()) {
                append("&blackList=")
                flag.forEachIndexed { index, currentFlag ->
                    append(currentFlag.name)
                    if (index < flag.size) {
                        append(JOKE_API_URL_SEPARATOR)
                    }
                }
            }
        }

        Logger.e("jokeURl is $jokeUrl")
        val response = client.get(BASE_URL + JOKE_ENDPOINT + jokeUrl) {
            addJokeApiHeaders()
        }
        val successResult = Json.decodeFromString<JokeResponseDTO>(response.bodyAsText())
        Logger.e("successResult was $successResult")
        return Result.success(successResult)
    }
}