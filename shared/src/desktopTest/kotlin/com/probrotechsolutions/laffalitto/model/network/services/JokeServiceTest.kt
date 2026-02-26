package com.probrotechsolutions.laffalitto.model.network.services

import com.probrotechsolutions.laffalitto.actual.EnvironmentVariables
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class JokeServiceTest {

    private val categoriesJson = """
        {
          "categories": ["Programming", "Misc"],
          "categoryAliases": [{"alias": "Dev", "resolved": "Programming"}],
          "error": false,
          "timestamp": 1234567890
        }
    """.trimIndent()

    private val singleJokeJson = """
        {
          "error": false,
          "category": "Programming",
          "type": "single",
          "joke": "Why do programmers prefer dark mode? Because light attracts bugs!",
          "id": 1,
          "safe": true,
          "lang": "en"
        }
    """.trimIndent()

    private val twoPartJokeJson = """
        {
          "error": false,
          "category": "Programming",
          "type": "twopart",
          "setup": "Why do Java developers wear glasses?",
          "delivery": "Because they don't C#!",
          "id": 2,
          "safe": true,
          "lang": "en"
        }
    """.trimIndent()

    private fun buildEngine(responseBody: String, status: HttpStatusCode = HttpStatusCode.OK) =
        MockEngine {
            respond(
                content = responseBody,
                status = status,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

    private fun buildRoutingEngine(categoriesBody: String, jokeBody: String, status: HttpStatusCode = HttpStatusCode.OK) =
        MockEngine { request ->
            val body = if ("categories" in request.url.encodedPath) categoriesBody else jokeBody
            respond(
                content = body,
                status = status,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

    @Test
    fun `getJokeCategories success response returns parsed DTO`() = runTest {
        val service = JokeService(EnvironmentVariables(), buildEngine(categoriesJson))
        val result = service.getJokeCategories()
        assertTrue(result.isSuccess)
        val dto = result.getOrNull()!!
        assertEquals(2, dto.categories.size)
        assertEquals("Programming", dto.categories[0])
        assertEquals("Misc", dto.categories[1])
    }

    @Test
    fun `getJokeCategories parses categoryAliases correctly`() = runTest {
        val service = JokeService(EnvironmentVariables(), buildEngine(categoriesJson))
        val result = service.getJokeCategories()
        val dto = result.getOrNull()!!
        assertEquals(1, dto.categoryAliases.size)
        assertEquals("Dev", dto.categoryAliases[0].alias)
        assertEquals("Programming", dto.categoryAliases[0].resolved)
    }

    @Test
    fun `getJokeCategories 404 response returns failure`() = runTest {
        val service = JokeService(EnvironmentVariables(), buildEngine("", HttpStatusCode.NotFound))
        val result = service.getJokeCategories()
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull()?.message?.contains("404") == true)
    }

    @Test
    fun `getJokeCategories 500 response returns failure`() = runTest {
        val service = JokeService(EnvironmentVariables(), buildEngine("", HttpStatusCode.InternalServerError))
        val result = service.getJokeCategories()
        assertTrue(result.isFailure)
    }

    @Test
    fun `getJoke success returns parsed single joke DTO`() = runTest {
        val service = JokeService(EnvironmentVariables(), buildEngine(singleJokeJson))
        val result = service.getJoke("Programming")
        assertTrue(result.isSuccess)
        val dto = result.getOrNull()!!
        assertEquals("Programming", dto.category)
        assertEquals("single", dto.type)
        assertTrue(dto.joke?.isNotEmpty() == true)
    }

    @Test
    fun `getJoke success returns parsed twopart joke DTO`() = runTest {
        val service = JokeService(EnvironmentVariables(), buildEngine(twoPartJokeJson))
        val result = service.getJoke("Programming")
        assertTrue(result.isSuccess)
        val dto = result.getOrNull()!!
        assertEquals("twopart", dto.type)
        assertTrue(dto.setup?.isNotEmpty() == true)
        assertTrue(dto.delivery?.isNotEmpty() == true)
    }

    @Test
    fun `getJoke 4xx response returns failure`() = runTest {
        val service = JokeService(EnvironmentVariables(), buildEngine("", HttpStatusCode.Unauthorized))
        val result = service.getJoke("Programming")
        assertTrue(result.isFailure)
    }

    @Test
    fun `routing engine serves categories and joke endpoints correctly`() = runTest {
        val engine = buildRoutingEngine(categoriesJson, singleJokeJson)
        val service = JokeService(EnvironmentVariables(), engine)

        val categoriesResult = service.getJokeCategories()
        val jokeResult = service.getJoke("Programming")

        assertTrue(categoriesResult.isSuccess)
        assertTrue(jokeResult.isSuccess)
        assertEquals(2, categoriesResult.getOrNull()!!.categories.size)
        assertEquals("single", jokeResult.getOrNull()!!.type)
    }
}
