package com.probrotechsolutions.laffalitto.fakes

import com.probrotechsolutions.laffalitto.model.local.jokes.Joke
import com.probrotechsolutions.laffalitto.model.local.jokes.JokeCategory
import com.probrotechsolutions.laffalitto.model.local.jokes.JokeType
import com.probrotechsolutions.laffalitto.model.repositories.JokeRepositoryInterface

class FakeJokeRepository(
    private val categoriesResult: Result<List<JokeCategory>> = Result.success(defaultCategories()),
    private val jokeResult: Result<Joke> = Result.success(defaultJoke())
) : JokeRepositoryInterface {
    override suspend fun getJokeCategories(): Result<List<JokeCategory>> = categoriesResult
    override suspend fun getJoke(category: String): Result<Joke> = jokeResult
}

fun defaultCategories() = listOf(
    JokeCategory(category = "Programming", aliases = emptyList())
)

fun defaultJoke() = Joke(
    category = "Programming",
    type = JokeType.SINGLE,
    joke = "Why do programmers prefer dark mode? Because light attracts bugs!"
)
