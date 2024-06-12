import kotlinx.coroutines.*

class ConversationHandler(private val gptClient: GPTClient) {
    private val conversationHistory = mutableListOf<String>()

    fun start() = runBlocking {
        while (true) {
            print("You: ")
            val userInput = readLine()
            if (userInput.isNullOrEmpty()) break
            conversationHistory.add("You: $userInput")
            val context = conversationHistory.joinToString("\n")
            val gptResponse = async { gptClient.getResponseSafely("Context: $context\nResponse:") }
            val response = gptResponse.await()
            println("Chatbot: $response")
            conversationHistory.add("Chatbot: $response")
        }
    }
}
