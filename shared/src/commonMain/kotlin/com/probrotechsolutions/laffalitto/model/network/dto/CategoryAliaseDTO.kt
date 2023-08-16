package com.probrotechsolutions.laffalitto.model.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class CategoryAliaseDTO(
    val alias: String,
    val resolved: String
)