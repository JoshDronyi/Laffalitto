package com.probrotechsolutions.laffalitto.model.mappers

import com.probrotechsolutions.laffalitto.Utils.toAlias
import com.probrotechsolutions.laffalitto.model.local.jokeCategory.JokeCategory
import com.probrotechsolutions.laffalitto.model.network.dto.jokeCategoryResponse.JokeCategoryResponseDTO

class JokeCategoryResponseMapper : Mapper<JokeCategoryResponseDTO, List<JokeCategory>> {
    override fun invoke(dto: JokeCategoryResponseDTO): List<JokeCategory> = with(dto) {
        categories.map { category ->
            categoryAliases.any { it.resolved == category }
            JokeCategory(
                category = category,
                aliases = categoryAliases.filter { it.resolved == category }.map { it.toAlias() }
            )
        }
    }
}