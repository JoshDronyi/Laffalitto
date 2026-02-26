package com.probrotechsolutions.laffalitto.utils

import com.probrotechsolutions.laffalitto.Utils.toAlias
import com.probrotechsolutions.laffalitto.model.network.dto.CategoryAliaseDTO
import kotlin.test.Test
import kotlin.test.assertEquals

class UtilsTest {

    @Test
    fun `toAlias maps alias field to Alias name`() {
        val dto = CategoryAliaseDTO(alias = "Dev", resolved = "Programming")
        val result = dto.toAlias()
        assertEquals("Dev", result.name)
    }

    @Test
    fun `toAlias maps resolved field to Alias resolved`() {
        val dto = CategoryAliaseDTO(alias = "Dev", resolved = "Programming")
        val result = dto.toAlias()
        assertEquals("Programming", result.resolved)
    }
}
