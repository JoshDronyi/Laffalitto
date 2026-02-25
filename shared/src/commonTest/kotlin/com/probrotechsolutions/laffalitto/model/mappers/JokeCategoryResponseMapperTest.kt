package com.probrotechsolutions.laffalitto.model.mappers

import com.probrotechsolutions.laffalitto.model.network.dto.CategoryAliaseDTO
import com.probrotechsolutions.laffalitto.model.network.dto.JokeCategoryResponseDTO
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JokeCategoryResponseMapperTest {

    private val mapper = JokeCategoryResponseMapper()

    // region: basic mapping

    @Test
    fun `maps category name onto JokeCategory`() {
        val dto = JokeCategoryResponseDTO(
            categories = listOf("Programming"),
            categoryAliases = emptyList(),
            error = false,
            timestamp = 0L
        )

        val result = mapper(dto)

        assertEquals(1, result.size)
        assertEquals("Programming", result[0].category)
    }

    @Test
    fun `maps all categories in order`() {
        val dto = JokeCategoryResponseDTO(
            categories = listOf("Programming", "Misc", "Dark"),
            categoryAliases = emptyList(),
            error = false,
            timestamp = 0L
        )

        val result = mapper(dto)

        assertEquals(listOf("Programming", "Misc", "Dark"), result.map { it.category })
    }

    // endregion

    // region: alias matching

    @Test
    fun `attaches a single matching alias to the correct category`() {
        val dto = JokeCategoryResponseDTO(
            categories = listOf("Programming"),
            categoryAliases = listOf(CategoryAliaseDTO(alias = "Coding", resolved = "Programming")),
            error = false,
            timestamp = 0L
        )

        val result = mapper(dto)

        assertEquals(1, result[0].aliases.size)
        assertEquals("Coding", result[0].aliases[0].name)
        assertEquals("Programming", result[0].aliases[0].resolved)
    }

    @Test
    fun `attaches multiple matching aliases to a category`() {
        val dto = JokeCategoryResponseDTO(
            categories = listOf("Programming"),
            categoryAliases = listOf(
                CategoryAliaseDTO(alias = "Coding", resolved = "Programming"),
                CategoryAliaseDTO(alias = "Dev", resolved = "Programming")
            ),
            error = false,
            timestamp = 0L
        )

        val result = mapper(dto)

        assertEquals(2, result[0].aliases.size)
        assertEquals(listOf("Coding", "Dev"), result[0].aliases.map { it.name })
    }

    @Test
    fun `only attaches aliases whose resolved field matches the category`() {
        val dto = JokeCategoryResponseDTO(
            categories = listOf("Programming", "Misc"),
            categoryAliases = listOf(
                CategoryAliaseDTO(alias = "Coding", resolved = "Programming"),
                CategoryAliaseDTO(alias = "Other", resolved = "Misc")
            ),
            error = false,
            timestamp = 0L
        )

        val result = mapper(dto)

        assertEquals(listOf("Coding"), result.first { it.category == "Programming" }.aliases.map { it.name })
        assertEquals(listOf("Other"), result.first { it.category == "Misc" }.aliases.map { it.name })
    }

    @Test
    fun `category with no matching alias gets empty aliases list`() {
        val dto = JokeCategoryResponseDTO(
            categories = listOf("Dark"),
            categoryAliases = listOf(CategoryAliaseDTO(alias = "Coding", resolved = "Programming")),
            error = false,
            timestamp = 0L
        )

        val result = mapper(dto)

        assertEquals(1, result.size)
        assertTrue(result[0].aliases.isEmpty())
    }

    // endregion

    // region: edge cases

    @Test
    fun `empty categories list produces empty result`() {
        val dto = JokeCategoryResponseDTO(
            categories = emptyList(),
            categoryAliases = listOf(CategoryAliaseDTO(alias = "Coding", resolved = "Programming")),
            error = false,
            timestamp = 0L
        )

        val result = mapper(dto)

        assertTrue(result.isEmpty())
    }

    @Test
    fun `empty aliases list produces JokeCategory objects with empty alias lists`() {
        val dto = JokeCategoryResponseDTO(
            categories = listOf("Programming", "Dark"),
            categoryAliases = emptyList(),
            error = false,
            timestamp = 0L
        )

        val result = mapper(dto)

        assertEquals(2, result.size)
        assertTrue(result.all { it.aliases.isEmpty() })
    }

    @Test
    fun `all empty produces empty result`() {
        val dto = JokeCategoryResponseDTO(
            categories = emptyList(),
            categoryAliases = emptyList(),
            error = false,
            timestamp = 0L
        )

        assertTrue(mapper(dto).isEmpty())
    }

    @Test
    fun `category is still included even when it has no matching alias`() {
        // This test documents that the dead `.any{}` call on line 10 of the mapper
        // does NOT gate inclusion — all categories appear in the output regardless
        // of whether they have a corresponding alias.
        val dto = JokeCategoryResponseDTO(
            categories = listOf("NoAliasCategory"),
            categoryAliases = listOf(CategoryAliaseDTO(alias = "Other", resolved = "DifferentCategory")),
            error = false,
            timestamp = 0L
        )

        val result = mapper(dto)

        assertEquals(1, result.size)
        assertEquals("NoAliasCategory", result[0].category)
        assertTrue(result[0].aliases.isEmpty())
    }

    // endregion
}
