package com.probrotechsolutions.laffalitto.model.repositories

import com.probrotechsolutions.laffalitto.model.local.jokes.JokeCategory
import com.probrotechsolutions.laffalitto.model.mappers.JokeCategoryResponseMapper
import com.probrotechsolutions.laffalitto.model.network.services.JokeService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class JokeRepository(
    private val jokeService: JokeService,
    private val jokeResponseMapper: JokeCategoryResponseMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    suspend fun getJokeCategories(): Result<List<JokeCategory>> = withContext(dispatcher) {
        try {
            val result = jokeService.getJokeCategories()
            val list = result.getOrNull()
            return@withContext list?.let {
                Result.success(jokeResponseMapper(list))
            } ?: Result.failure(result.exceptionOrNull() ?: Throwable("Unknown Error."))
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }
}