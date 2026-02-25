package com.probrotechsolutions.laffalitto.model.network.services

import com.probrotechsolutions.laffalitto.actual.EnvironmentVariablesContract
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertTrue

class JokeServiceTest {

    // region: fakes

    private val fakeEnv = object : EnvironmentVariablesContract {
        override val apiKey = "test-api-key"
        override val host = "test-host"
    }

    private val validResponseJson = """
        {
            "error": false,
            "categories": ["Programming", "Misc", "Dark", "Pun", "Spooky", "Christmas"],
            "categoryAliases": [
                { "alias": "Coding", "resolved": "Programming" },
                { "alias": "Dev", "resolved": "Programming" }
            ],
            "timestamp": 1696000000000
        }
    """.trimIndent()

    private fun makeService(engine: MockEngine) = JokeService(
        environmentVariables = fakeEnv,
        httpClient = HttpClient(engine)
    )

    // endregion

    // region: correct URL

    @Test
    fun `getJokeCategories requests the correct URL`() = runTest {
        var capturedUrl: String? = null
        val engine = MockEngine { request ->
            capturedUrl = request.url.toString()
            respond(
                content = validResponseJson,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        makeService(engine).getJokeCategories()

        assertEquals("https://jokeapi-v2.p.rapidapi.com/categories?format=json", capturedUrl)
    }

    // endregion

    // region: correct headers

    @Test
    fun `getJokeCategories sends the api key header`() = runTest {
        var capturedHeaders: Headers? = null
        val engine = MockEngine { request ->
            capturedHeaders = request.headers
            respond(
                content = validResponseJson,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        makeService(engine).getJokeCategories()

        assertEquals("test-api-key", capturedHeaders?.get("x-rapidApi-key"))
    }

    @Test
    fun `getJokeCategories sends the host header`() = runTest {
        var capturedHeaders: Headers? = null
        val engine = MockEngine { request ->
            capturedHeaders = request.headers
            respond(
                content = validResponseJson,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        makeService(engine).getJokeCategories()

        assertEquals("test-host", capturedHeaders?.get("x-rapidApi-host"))
    }

    // endregion

    // region: successful deserialization

    @Test
    fun `getJokeCategories deserializes categories list`() = runTest {
        val engine = MockEngine { _ ->
            respond(
                content = validResponseJson,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val result = makeService(engine).getJokeCategories()

        assertTrue(result.isSuccess)
        val dto = result.getOrThrow()
        assertEquals(listOf("Programming", "Misc", "Dark", "Pun", "Spooky", "Christmas"), dto.categories)
    }

    @Test
    fun `getJokeCategories deserializes categoryAliases list`() = runTest {
        val engine = MockEngine { _ ->
            respond(
                content = validResponseJson,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val result = makeService(engine).getJokeCategories()

        val dto = result.getOrThrow()
        assertEquals(2, dto.categoryAliases.size)
        assertEquals("Coding", dto.categoryAliases[0].alias)
        assertEquals("Programming", dto.categoryAliases[0].resolved)
    }

    @Test
    fun `getJokeCategories deserializes error flag`() = runTest {
        val engine = MockEngine { _ ->
            respond(
                content = validResponseJson,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val dto = makeService(engine).getJokeCategories().getOrThrow()

        assertEquals(false, dto.error)
    }

    // endregion

    // region: error handling gap

    @Test
    fun `getJokeCategories throws when response body is malformed JSON`() = runTest {
        val engine = MockEngine { _ ->
            respond(
                content = "not valid json {{{",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        // JokeService has no error handling — serialization failures propagate as exceptions.
        // JokeRepository's try/catch will eventually catch this, but JokeService itself
        // does not wrap it in Result.failure. This test documents that behaviour.
        assertFails {
            makeService(engine).getJokeCategories()
        }
    }

    // endregion
}
