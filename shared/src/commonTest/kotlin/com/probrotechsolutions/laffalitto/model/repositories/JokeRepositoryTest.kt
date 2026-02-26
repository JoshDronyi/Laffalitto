package com.probrotechsolutions.laffalitto.model.repositories

import com.probrotechsolutions.laffalitto.fakes.FakeJokeService
import com.probrotechsolutions.laffalitto.fakes.defaultCategoriesDTO
import com.probrotechsolutions.laffalitto.model.local.jokes.JokeType
import com.probrotechsolutions.laffalitto.model.mappers.JokeCategoryResponseMapper
import com.probrotechsolutions.laffalitto.model.mappers.JokeResponseMapper
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JokeRepositoryTest {

    private fun makeRepo(service: FakeJokeService = FakeJokeService()) = JokeRepository(
        jokeService = service,
        jokeResponseMapper = JokeCategoryResponseMapper(),
        jokeMapper = JokeResponseMapper(),
        dispatcher = UnconfinedTestDispatcher()
    )

    @Test
    fun `getJokeCategories success maps DTO to domain list`() = runTest {
        val result = makeRepo().getJokeCategories()
        assertTrue(result.isSuccess)
        val categories = result.getOrNull()!!
        assertEquals(2, categories.size)
        assertEquals("Programming", categories[0].category)
        assertEquals("Misc", categories[1].category)
    }

    @Test
    fun `getJokeCategories aliases are correctly mapped`() = runTest {
        val result = makeRepo().getJokeCategories()
        val categories = result.getOrNull()!!
        val programming = categories.first { it.category == "Programming" }
        assertEquals(1, programming.aliases.size)
        assertEquals("Dev", programming.aliases[0].name)
    }

    @Test
    fun `getJokeCategories service failure propagates as Result failure`() = runTest {
        val service = FakeJokeService(categoriesResult = Result.failure(Throwable("Network error")))
        val result = makeRepo(service).getJokeCategories()
        assertTrue(result.isFailure)
        assertEquals("Network error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getJoke success maps DTO to Joke domain model`() = runTest {
        val result = makeRepo().getJoke("Programming")
        assertTrue(result.isSuccess)
        val joke = result.getOrNull()!!
        assertEquals("Programming", joke.category)
        assertEquals(JokeType.SINGLE, joke.type)
    }

    @Test
    fun `getJoke single type has joke field set`() = runTest {
        val result = makeRepo().getJoke("Programming")
        val joke = result.getOrNull()!!
        assertTrue(joke.joke?.isNotEmpty() == true)
    }

    @Test
    fun `getJoke service failure propagates as Result failure`() = runTest {
        val service = FakeJokeService(jokeResult = Result.failure(Throwable("Server error")))
        val result = makeRepo(service).getJoke("Programming")
        assertTrue(result.isFailure)
        assertEquals("Server error", result.exceptionOrNull()?.message)
    }

    @Test
    fun `getJokeCategories returns failure on empty service success`() = runTest {
        val emptyDto = defaultCategoriesDTO().copy(categories = emptyList(), categoryAliases = emptyList())
        val service = FakeJokeService(categoriesResult = Result.success(emptyDto))
        val result = makeRepo(service).getJokeCategories()
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()!!.isEmpty())
    }
}
