package com.probrotechsolutions.laffalitto.fakes

import com.probrotechsolutions.laffalitto.model.network.dto.CategoryAliaseDTO
import com.probrotechsolutions.laffalitto.model.network.dto.JokeCategoryResponseDTO
import com.probrotechsolutions.laffalitto.model.network.dto.JokeResponseDTO
import com.probrotechsolutions.laffalitto.model.network.services.JokeServiceInterface

class FakeJokeService(
    private val categoriesResult: Result<JokeCategoryResponseDTO> = Result.success(defaultCategoriesDTO()),
    private val jokeResult: Result<JokeResponseDTO> = Result.success(defaultJokeDTO())
) : JokeServiceInterface {
    override suspend fun getJokeCategories(): Result<JokeCategoryResponseDTO> = categoriesResult
    override suspend fun getJoke(category: String): Result<JokeResponseDTO> = jokeResult
}

fun defaultCategoriesDTO() = JokeCategoryResponseDTO(
    categories = listOf("Programming", "Misc"),
    categoryAliases = listOf(CategoryAliaseDTO(alias = "Dev", resolved = "Programming")),
    error = false,
    timestamp = 1234567890L
)

fun defaultJokeDTO() = JokeResponseDTO(
    error = false,
    category = "Programming",
    type = "single",
    joke = "Why do programmers prefer dark mode? Because light attracts bugs!",
    id = 1,
    safe = true,
    lang = "en"
)
