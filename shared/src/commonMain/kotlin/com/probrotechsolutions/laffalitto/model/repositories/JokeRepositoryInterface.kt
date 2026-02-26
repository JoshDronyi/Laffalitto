package com.probrotechsolutions.laffalitto.model.repositories

import com.probrotechsolutions.laffalitto.model.local.jokes.Joke
import com.probrotechsolutions.laffalitto.model.local.jokes.JokeCategory

interface JokeRepositoryInterface {
    suspend fun getJokeCategories(): Result<List<JokeCategory>>
    suspend fun getJoke(category: String): Result<Joke>
}
