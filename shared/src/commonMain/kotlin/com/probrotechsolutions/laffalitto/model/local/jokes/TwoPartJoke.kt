package com.probrotechsolutions.laffalitto.model.local.jokes

import com.probrotechsolutions.laffalitto.model.local.flags.Flag

data class TwoPartJoke(
    val setUp: String = "",
    val delivery: String = "",
    override val id: Int,
    override val lang: String,
    override val flagList: List<Flag>,
    override val category: String,
    override val safe: Boolean
) : BaseJoke()
