package com.probrotechsolutions.laffalitto.model.network.dto.JokeResponse

import kotlinx.serialization.Serializable

@Serializable
data class FlagList(
    val explicit: Boolean = false,
    val nsfw: Boolean = false,
    val political: Boolean = false,
    val racist: Boolean = false,
    val religious: Boolean = false,
    val sexist: Boolean = false
)