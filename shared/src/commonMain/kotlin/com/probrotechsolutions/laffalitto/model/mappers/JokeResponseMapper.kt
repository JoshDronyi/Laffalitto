package com.probrotechsolutions.laffalitto.model.mappers

import com.probrotechsolutions.laffalitto.model.local.flags.Flag
import com.probrotechsolutions.laffalitto.model.local.jokes.BaseJoke
import com.probrotechsolutions.laffalitto.model.local.jokes.OneLineJoke
import com.probrotechsolutions.laffalitto.model.local.jokes.TwoPartJoke
import com.probrotechsolutions.laffalitto.model.network.dto.JokeResponse.FlagList
import com.probrotechsolutions.laffalitto.model.network.dto.JokeResponse.JokeResponseDTO

class JokeResponseMapper : Mapper<JokeResponseDTO, BaseJoke> {
    override fun invoke(dto: JokeResponseDTO): BaseJoke = with(dto) {
        return when (dto.category) {
            JokeCategory.TWO_PART.name -> {
                TwoPartJoke(
                    id = id,
                    setUp = setUp,
                    delivery = delivery,
                    lang = lang,
                    flagList = flags.toList(),
                    category = category,
                    safe = safe
                )
            }

            else -> OneLineJoke(
                id = id,
                joke = joke,
                category = category,
                flagList = flags.toList(),
                lang = lang,
                safe = safe
            )
        }
    }
}

fun FlagList.toList(): List<Flag> {
    return mutableListOf<Flag>().apply {
        if (explicit) add(Flag.EXPLICIT)
        if (nsfw) add(Flag.NSFW)
        if (political) add(Flag.POLITICAL)
        if (racist) add(Flag.RACIST)
        if (sexist) add(Flag.SEXIST)
    }
}


enum class JokeCategory(name: String) {
    ONE_LINE("single"), TWO_PART("twopart")
}