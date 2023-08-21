package com.probrotechsolutions.laffalitto.model.repositories

import com.probrotechsolutions.laffalitto.model.local.flags.Flag
import com.probrotechsolutions.laffalitto.model.local.jokeCategory.JokeCategory
import com.probrotechsolutions.laffalitto.model.local.jokes.BaseJoke
import com.probrotechsolutions.laffalitto.model.mappers.JokeCategoryResponseMapper
import com.probrotechsolutions.laffalitto.model.mappers.JokeResponseMapper
import com.probrotechsolutions.laffalitto.model.network.services.JokeService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class JokeRepository(
    private val jokeService: JokeService,
    private val jokeResponseMapper: JokeCategoryResponseMapper,
    private val jokeMapper: JokeResponseMapper,
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

    suspend fun getFlags(): Result<List<Flag>> = withContext(dispatcher) {
        return@withContext try {
            val result = jokeService.getFlags()
            val list = result.getOrNull()?.flags ?: emptyList()
            Result.success(list.map {
                when (it) {
                    Flag.EXPLICIT.name -> {
                        Flag.EXPLICIT
                    }

                    Flag.SEXIST.name -> {
                        Flag.SEXIST
                    }

                    Flag.RELIGIOUS.name -> {
                        Flag.RELIGIOUS
                    }

                    Flag.RACIST.name -> {
                        Flag.RACIST
                    }

                    Flag.POLITICAL.name -> {
                        Flag.POLITICAL
                    }

                    else -> {
                        Flag.NSFW
                    }
                }
            })
        } catch (ex: Exception) {
            Result.failure(ex)
        }
    }

    suspend fun getJoke(category: String, vararg flag: Flag): Result<BaseJoke> =
        withContext(dispatcher) {
            return@withContext try {
                val jokeResult = jokeService.getJoke(category, *flag)
                val response = jokeResult.getOrNull()
                response?.let {
                    Result.success(jokeMapper(it))
                } ?: Result.failure(Throwable("Unknown error"))
            } catch (ex: Throwable) {
                Result.failure(ex)
            }
        }
}