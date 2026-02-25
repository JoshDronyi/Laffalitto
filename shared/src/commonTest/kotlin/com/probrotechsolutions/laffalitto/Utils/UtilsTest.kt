package com.probrotechsolutions.laffalitto.Utils

import com.probrotechsolutions.laffalitto.model.network.dto.CategoryAliaseDTO
import kotlin.test.Test
import kotlin.test.assertEquals

class UtilsTest {

    // region: field mapping

    @Test
    fun `toAlias maps alias field to Alias name`() {
        val dto = CategoryAliaseDTO(alias = "Coding", resolved = "Programming")

        val result = dto.toAlias()

        assertEquals("Coding", result.name)
    }

    @Test
    fun `toAlias maps resolved field to Alias resolved`() {
        val dto = CategoryAliaseDTO(alias = "Coding", resolved = "Programming")

        val result = dto.toAlias()

        assertEquals("Programming", result.resolved)
    }

    // endregion

    // region: edge cases

    @Test
    fun `toAlias preserves empty string alias`() {
        val dto = CategoryAliaseDTO(alias = "", resolved = "Programming")

        val result = dto.toAlias()

        assertEquals("", result.name)
    }

    @Test
    fun `toAlias preserves empty string resolved`() {
        val dto = CategoryAliaseDTO(alias = "Coding", resolved = "")

        val result = dto.toAlias()

        assertEquals("", result.resolved)
    }

    @Test
    fun `toAlias preserves both fields when both are empty`() {
        val dto = CategoryAliaseDTO(alias = "", resolved = "")

        val result = dto.toAlias()

        assertEquals("", result.name)
        assertEquals("", result.resolved)
    }

    // endregion
}
