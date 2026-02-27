package com.probrotechsolutions.laffalitto.model.mappers

import com.probrotechsolutions.laffalitto.model.local.jokes.Joke
import com.probrotechsolutions.laffalitto.model.local.jokes.JokeType
import com.probrotechsolutions.laffalitto.model.network.dto.JokeResponseDTO

class JokeResponseMapper : Mapper<JokeResponseDTO, Joke> {
    override fun invoke(dto: JokeResponseDTO): Joke = Joke(
        category = dto.category,
        type = if (dto.type == "single") JokeType.SINGLE else JokeType.TWO_PART,
        joke = dto.joke,
        setup = dto.setup,
        delivery = dto.delivery
    )
}
