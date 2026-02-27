package com.probrotechsolutions.laffalitto.model.network.services

import com.probrotechsolutions.laffalitto.model.network.dto.JokeCategoryResponseDTO
import com.probrotechsolutions.laffalitto.model.network.dto.JokeResponseDTO

interface JokeServiceInterface {
    suspend fun getJokeCategories(): Result<JokeCategoryResponseDTO>
    suspend fun getJoke(category: String): Result<JokeResponseDTO>
}
