package com.probrotechsolutions.laffalitto.model.repositories

import com.probrotechsolutions.laffalitto.model.mappers.JokeCategoryResponseMapper
import com.probrotechsolutions.laffalitto.model.network.dto.CategoryAliaseDTO
import com.probrotechsolutions.laffalitto.model.network.dto.JokeCategoryResponseDTO
import com.probrotechsolutions.laffalitto.model.network.services.JokeServiceContract
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JokeRepositoryTest {

    // region: fakes

    private class FakeJokeService(
        private val response: Result<JokeCategoryResponseDTO>
    ) : JokeServiceContract {
        override suspend fun getJokeCategories() = response
    }

    private class ThrowingJokeService(
        private val exception: Exception
    ) : JokeServiceContract {
        override suspend fun getJokeCategories(): Result<JokeCategoryResponseDTO> {
            throw exception
        }
    }

    private val sampleDto = JokeCategoryResponseDTO(
        categories = listOf("Programming", "Dark"),
        categoryAliases = listOf(
            CategoryAliaseDTO(alias = "Coding", resolved = "Programming")
        ),
        error = false,
        timestamp = 0L
    )

    private fun makeRepository(service: JokeServiceContract) = JokeRepository(
        jokeService = service,
        jokeResponseMapper = JokeCategoryResponseMapper(),
        dispatcher = StandardTestDispatcher()
    )

    // endregion

    // region: success path

    @Test
    fun `returns success with mapped categories when service succeeds`() = runTest {
        val repo = makeRepository(FakeJokeService(Result.success(sampleDto)))

        val result = repo.getJokeCategories()

        assertTrue(result.isSuccess)
        val categories = result.getOrThrow()
        assertEquals(2, categories.size)
        assertEquals("Programming", categories[0].category)
        assertEquals("Dark", categories[1].category)
    }

    @Test
    fun `maps aliases onto matching categories`() = runTest {
        val repo = makeRepository(FakeJokeService(Result.success(sampleDto)))

        val categories = repo.getJokeCategories().getOrThrow()

        val programming = categories.first { it.category == "Programming" }
        assertEquals(1, programming.aliases.size)
        assertEquals("Coding", programming.aliases[0].name)
    }

    @Test
    fun `returns empty list when service returns dto with no categories`() = runTest {
        val emptyDto = JokeCategoryResponseDTO(
            categories = emptyList(),
            categoryAliases = emptyList(),
            error = false,
            timestamp = 0L
        )
        val repo = makeRepository(FakeJokeService(Result.success(emptyDto)))

        val result = repo.getJokeCategories()

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
    }

    // endregion

    // region: failure path — service returns Result.failure

    @Test
    fun `returns failure when service returns Result failure`() = runTest {
        val error = RuntimeException("network error")
        val repo = makeRepository(FakeJokeService(Result.failure(error)))

        val result = repo.getJokeCategories()

        assertFalse(result.isSuccess)
    }

    @Test
    fun `propagates original exception message when service returns Result failure`() = runTest {
        val error = RuntimeException("network error")
        val repo = makeRepository(FakeJokeService(Result.failure(error)))

        val result = repo.getJokeCategories()

        assertEquals("network error", result.exceptionOrNull()?.message)
    }

    // endregion

    // region: failure path — service throws

    @Test
    fun `returns failure when service throws an exception`() = runTest {
        val repo = makeRepository(ThrowingJokeService(RuntimeException("timeout")))

        val result = repo.getJokeCategories()

        assertFalse(result.isSuccess)
    }

    @Test
    fun `wraps thrown exception in Result failure`() = runTest {
        val repo = makeRepository(ThrowingJokeService(RuntimeException("timeout")))

        val result = repo.getJokeCategories()

        assertEquals("timeout", result.exceptionOrNull()?.message)
    }

    // endregion
}
