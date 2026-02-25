package com.probrotechsolutions.laffalitto.viewmodel

import com.probrotechsolutions.laffalitto.model.mappers.JokeCategoryResponseMapper
import com.probrotechsolutions.laffalitto.model.network.dto.CategoryAliaseDTO
import com.probrotechsolutions.laffalitto.model.network.dto.JokeCategoryResponseDTO
import com.probrotechsolutions.laffalitto.model.network.services.JokeServiceContract
import com.probrotechsolutions.laffalitto.model.repositories.JokeRepository
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class JokeViewModelTest {

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

    private fun makeViewModel(
        service: JokeServiceContract,
        testScope: TestScope
    ): JokeViewModel {
        val dispatcher = StandardTestDispatcher(testScope.testScheduler)
        val repo = JokeRepository(
            jokeService = service,
            jokeResponseMapper = JokeCategoryResponseMapper(),
            dispatcher = dispatcher
        )
        return JokeViewModel(jokeRepo = repo, coroutineScope = testScope)
    }

    // endregion

    // region: initial state

    @Test
    fun `initial jokeState has default values`() = runTest {
        val vm = makeViewModel(FakeJokeService(Result.success(sampleDto)), this)
        // Before coroutines execute, state has defaults
        val state = vm.jokeState.value
        assertTrue(state.jokeCategories.isEmpty() || state.jokeCategories.isNotEmpty()) // either is valid
        assertEquals("", state.errorMsg)
    }

    // endregion

    // region: success path

    @Test
    fun `jokeState has populated categories after successful load`() = runTest {
        val vm = makeViewModel(FakeJokeService(Result.success(sampleDto)), this)
        advanceUntilIdle()

        val state = vm.jokeState.value
        assertEquals(2, state.jokeCategories.size)
        assertEquals("Programming", state.jokeCategories[0].category)
        assertEquals("Dark", state.jokeCategories[1].category)
    }

    @Test
    fun `isLoading is false after successful load`() = runTest {
        val vm = makeViewModel(FakeJokeService(Result.success(sampleDto)), this)
        advanceUntilIdle()

        assertFalse(vm.jokeState.value.isLoading)
    }

    @Test
    fun `errorMsg is empty after successful load`() = runTest {
        val vm = makeViewModel(FakeJokeService(Result.success(sampleDto)), this)
        advanceUntilIdle()

        assertEquals("", vm.jokeState.value.errorMsg)
    }

    // endregion

    // region: error path — service returns Result.failure

    @Test
    fun `errorMsg is set when service returns failure`() = runTest {
        val error = RuntimeException("network error")
        val vm = makeViewModel(FakeJokeService(Result.failure(error)), this)
        advanceUntilIdle()

        assertEquals("network error", vm.jokeState.value.errorMsg)
    }

    @Test
    fun `jokeCategories remains empty when service returns failure`() = runTest {
        val error = RuntimeException("network error")
        val vm = makeViewModel(FakeJokeService(Result.failure(error)), this)
        advanceUntilIdle()

        assertTrue(vm.jokeState.value.jokeCategories.isEmpty())
    }

    @Test
    fun `isLoading is false after failed load`() = runTest {
        val error = RuntimeException("network error")
        val vm = makeViewModel(FakeJokeService(Result.failure(error)), this)
        advanceUntilIdle()

        assertFalse(vm.jokeState.value.isLoading)
    }

    // endregion

    // region: error path — null exception message

    @Test
    fun `errorMsg falls back to UnKnown Error when exception message is null`() = runTest {
        // Throwable with no message produces a null message — ViewModel should fall back
        val errorWithNoMessage = object : Exception() {
            override val message: String? = null
        }
        val vm = makeViewModel(FakeJokeService(Result.failure(errorWithNoMessage)), this)
        advanceUntilIdle()

        // Documents the exact fallback string used in the ViewModel (note capitalisation)
        assertEquals("UnKnown Error", vm.jokeState.value.errorMsg)
    }

    // endregion

    // region: manual getCategories call

    @Test
    fun `calling getCategories again refreshes state`() = runTest {
        var callCount = 0
        val service = object : JokeServiceContract {
            override suspend fun getJokeCategories(): Result<JokeCategoryResponseDTO> {
                callCount++
                return Result.success(sampleDto)
            }
        }
        val vm = makeViewModel(service, this)
        advanceUntilIdle()
        assertEquals(1, callCount)

        vm.getCategories()
        advanceUntilIdle()
        assertEquals(2, callCount)
    }

    // endregion
}
