import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.json.JSONObject
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class GPTClientTest {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var gptClient: GPTClient

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        gptClient = GPTClient("test-api-key").apply {
            baseUrl = mockWebServer.url("/").toString()
        }
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `test getResponse returns expected response`() {
        val mockResponse = MockResponse()
            .setResponseCode(200)
            .setBody(JSONObject().apply {
                put("choices", listOf(
                    JSONObject().apply {
                        put("message", JSONObject().apply {
                            put("role", "assistant")
                            put("content", "Hello, world!")
                        })
                    }
                ))
            }.toString())

        mockWebServer.enqueue(mockResponse)

        val response = gptClient.getResponse("Hello")
        assertNotNull(response)
        assertEquals("Hello, world!", response)
    }

    @Test
    fun `test getResponseSafely handles exceptions gracefully`() {
        val mockResponse = MockResponse()
            .setResponseCode(500)
            .setBody("Internal Server Error")

        mockWebServer.enqueue(mockResponse)

        val response = gptClient.getResponseSafely("Hello")
        assertEquals("Error: No response from GPT.", response)
    }
}
