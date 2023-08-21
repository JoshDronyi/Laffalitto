package com.probrotechsolutions.laffalitto.model.network.dto.flagResponse

import kotlinx.serialization.Serializable

@Serializable
data class FlagListResponseDTO(
    val error: Boolean,
    val flags: List<String>,
    val timestamp: Long
)