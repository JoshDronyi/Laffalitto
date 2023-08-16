package com.probrotechsolutions.laffalitto.model.local.jokes

data class JokeCategory(
    val category: String = "",
    val aliases: List<Alias> = emptyList()
)