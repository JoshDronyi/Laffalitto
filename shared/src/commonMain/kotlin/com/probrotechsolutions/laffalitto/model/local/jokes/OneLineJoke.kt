package com.probrotechsolutions.laffalitto.model.local.jokes

import com.probrotechsolutions.laffalitto.model.local.flags.Flag

data class OneLineJoke(
    val joke: String = "Ya face.",
    override val category: String,
    override val flagList: List<Flag>,
    override val id: Int,
    override val lang: String,
    override val safe: Boolean
) : BaseJoke()