package com.probrotechsolutions.laffalitto.model.network.services

import com.probrotechsolutions.laffalitto.model.network.dto.JokeCategoryResponseDTO

interface JokeServiceContract {
    suspend fun getJokeCategories(): Result<JokeCategoryResponseDTO>
}
