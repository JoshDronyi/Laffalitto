package com.probrotechsolutions.laffalitto.model.local.jokes

enum class JokeType { SINGLE, TWO_PART }

data class Joke(
    val category: String,
    val type: JokeType,
    val joke: String? = null,
    val setup: String? = null,
    val delivery: String? = null
)
