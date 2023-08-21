package com.probrotechsolutions.laffalitto.model.network.dto.jokeCategoryResponse

import kotlinx.serialization.Serializable

@Serializable
data class CategoryAliaseDTO(
    val alias: String,
    val resolved: String
)