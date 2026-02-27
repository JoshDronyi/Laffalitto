package com.probrotechsolutions.laffalitto.model.mappers

import com.probrotechsolutions.laffalitto.model.local.jokes.JokeType
import com.probrotechsolutions.laffalitto.model.network.dto.JokeResponseDTO
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class JokeResponseMapperTest {

    private val mapper = JokeResponseMapper()

    private fun makeDto(type: String = "single", joke: String? = "Why?", setup: String? = null, delivery: String? = null) =
        JokeResponseDTO(error = false, category = "Programming", type = type, joke = joke, setup = setup, delivery = delivery, id = 1, safe = true, lang = "en")

    @Test
    fun `single type maps to JokeType SINGLE`() {
        val result = mapper(makeDto(type = "single"))
        assertEquals(JokeType.SINGLE, result.type)
    }

    @Test
    fun `twopart type maps to JokeType TWO_PART`() {
        val result = mapper(makeDto(type = "twopart", joke = null, setup = "Why?", delivery = "Because!"))
        assertEquals(JokeType.TWO_PART, result.type)
    }

    @Test
    fun `unknown type string defaults to TWO_PART`() {
        val result = mapper(makeDto(type = "unknown"))
        assertEquals(JokeType.TWO_PART, result.type)
    }

    @Test
    fun `category is preserved from DTO`() {
        val dto = makeDto().copy(category = "Misc")
        val result = mapper(dto)
        assertEquals("Misc", result.category)
    }

    @Test
    fun `joke field is preserved from single DTO`() {
        val result = mapper(makeDto(joke = "A funny joke"))
        assertEquals("A funny joke", result.joke)
    }

    @Test
    fun `setup and delivery preserved from twopart DTO`() {
        val result = mapper(makeDto(type = "twopart", joke = null, setup = "Setup?", delivery = "Punchline!"))
        assertEquals("Setup?", result.setup)
        assertEquals("Punchline!", result.delivery)
    }

    @Test
    fun `joke is null on twopart DTO`() {
        val result = mapper(makeDto(type = "twopart", joke = null, setup = "Setup?", delivery = "Punchline!"))
        assertNull(result.joke)
    }
}
