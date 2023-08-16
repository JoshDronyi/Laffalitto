package com.probrotechsolutions.laffalitto.model.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class JokeCategoryResponseDTO(
    val categories: List<String>,
    val categoryAliases: List<CategoryAliaseDTO>,
    val error: Boolean,
    val timestamp: Long
)