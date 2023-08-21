package com.probrotechsolutions.laffalitto.model.local.jokes

import com.probrotechsolutions.laffalitto.model.local.flags.Flag

abstract class BaseJoke {
    open val id: Int = 0
    open val category: String = ""
    open val safe: Boolean = false
    open val lang: String = "en"
    open val flagList: List<Flag> = emptyList()
}