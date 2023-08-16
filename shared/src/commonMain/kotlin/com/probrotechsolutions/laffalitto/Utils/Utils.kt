package com.probrotechsolutions.laffalitto.Utils

import com.probrotechsolutions.laffalitto.model.local.jokes.Alias
import com.probrotechsolutions.laffalitto.model.network.dto.CategoryAliaseDTO

fun CategoryAliaseDTO.toAlias(): Alias {
    return Alias(
        name = alias,
        resolved = resolved
    )
}