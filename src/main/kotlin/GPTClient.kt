import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.json.JSONObject

class GPTClient(private val apiKey: String) {
    private val client = OkHttpClient()

    fun getResponse(prompt: String): String? {
        val requestBody = JSONObject()
            .put("model", "gpt-3.5-turbo")
            .put("messages", listOf(
                mapOf("role" to "user", "content" to prompt)
            ))
            .put("max_tokens", 100)
            .toString()

        val request = Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .post(RequestBody.create("application/json".toMediaTypeOrNull(), requestBody))
            .addHeader("Authorization", "Bearer $apiKey")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                println("Error: ${response.code}")
                println("Error Body: ${response.body?.string()}")
                return null
            } else {
                val responseBody = response.body?.string()
                return JSONObject(responseBody)
                    .getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content")
            }
        }
    }

    fun getResponseSafely(prompt: String): String {
        return try {
            val response = getResponse(prompt)
            response ?: "Error: No response from GPT."
        } catch (e: Exception) {
            println("Exception: ${e.message}")
            "Exception: ${e.message}"
        }
    }
}
