package com.probrotechsolutions.laffalitto.Utils

import com.probrotechsolutions.laffalitto.model.local.jokeCategory.Alias
import com.probrotechsolutions.laffalitto.model.network.dto.jokeCategoryResponse.CategoryAliaseDTO

fun CategoryAliaseDTO.toAlias(): Alias {
    return Alias(
        name = alias,
        resolved = resolved
    )
}