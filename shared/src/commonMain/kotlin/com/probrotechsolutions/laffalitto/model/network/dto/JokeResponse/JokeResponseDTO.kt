package com.probrotechsolutions.laffalitto.model.network.dto.JokeResponse

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JokeResponseDTO(
    @SerialName("category")
    val category: String = "",
    @SerialName("error")
    val error: Boolean = false,
    @SerialName("flags")
    val flags: FlagList = FlagList(),
    @SerialName("id")
    val id: Int = 0,
    @SerialName("setup")
    val setUp: String = "",
    @SerialName("delivery")
    val delivery: String = "",
    @SerialName("joke")
    val joke: String = "",
    @SerialName("lang")
    val lang: String = "",
    @SerialName("safe")
    val safe: Boolean = false,
    @SerialName("type")
    val type: String = ""
)