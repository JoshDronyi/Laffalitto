package com.probrotechsolutions.laffalitto.model.local.jokeCategory

data class JokeCategory(
    val category: String = "",
    val aliases: List<Alias> = emptyList()
)