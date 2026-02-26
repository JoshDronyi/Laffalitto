package com.probrotechsolutions.laffalitto.model.mappers

import com.probrotechsolutions.laffalitto.model.network.dto.CategoryAliaseDTO
import com.probrotechsolutions.laffalitto.model.network.dto.JokeCategoryResponseDTO
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JokeCategoryResponseMapperTest {

    private val mapper = JokeCategoryResponseMapper()

    @Test
    fun `empty categories list returns empty list`() {
        val dto = JokeCategoryResponseDTO(
            categories = emptyList(),
            categoryAliases = emptyList(),
            error = false,
            timestamp = 0L
        )
        val result = mapper(dto)
        assertTrue(result.isEmpty())
    }

    @Test
    fun `categories with no matching aliases get empty alias lists`() {
        val dto = JokeCategoryResponseDTO(
            categories = listOf("Programming"),
            categoryAliases = emptyList(),
            error = false,
            timestamp = 0L
        )
        val result = mapper(dto)
        assertEquals(1, result.size)
        assertEquals("Programming", result[0].category)
        assertTrue(result[0].aliases.isEmpty())
    }

    @Test
    fun `categories with matching aliases are mapped correctly`() {
        val dto = JokeCategoryResponseDTO(
            categories = listOf("Programming"),
            categoryAliases = listOf(CategoryAliaseDTO(alias = "Dev", resolved = "Programming")),
            error = false,
            timestamp = 0L
        )
        val result = mapper(dto)
        assertEquals(1, result.size)
        assertEquals(1, result[0].aliases.size)
        assertEquals("Dev", result[0].aliases[0].name)
        assertEquals("Programming", result[0].aliases[0].resolved)
    }

    @Test
    fun `multiple aliases for same category are all included`() {
        val dto = JokeCategoryResponseDTO(
            categories = listOf("Programming"),
            categoryAliases = listOf(
                CategoryAliaseDTO(alias = "Dev", resolved = "Programming"),
                CategoryAliaseDTO(alias = "Coding", resolved = "Programming")
            ),
            error = false,
            timestamp = 0L
        )
        val result = mapper(dto)
        assertEquals(2, result[0].aliases.size)
    }

    @Test
    fun `aliases not matching any category are not included in results`() {
        val dto = JokeCategoryResponseDTO(
            categories = listOf("Programming"),
            categoryAliases = listOf(CategoryAliaseDTO(alias = "Funny", resolved = "Misc")),
            error = false,
            timestamp = 0L
        )
        val result = mapper(dto)
        assertTrue(result[0].aliases.isEmpty())
    }
}
