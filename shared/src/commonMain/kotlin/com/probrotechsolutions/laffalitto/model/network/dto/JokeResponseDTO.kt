package com.probrotechsolutions.laffalitto.model.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class JokeResponseDTO(
    val error: Boolean,
    val category: String,
    val type: String,           // "single" or "twopart"
    val joke: String? = null,   // present when type == "single"
    val setup: String? = null,  // present when type == "twopart"
    val delivery: String? = null, // present when type == "twopart"
    val id: Int,
    val safe: Boolean,
    val lang: String
)
