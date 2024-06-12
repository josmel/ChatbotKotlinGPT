fun main() {
    val apiKey =
        System.getenv("YOUR_API_KEY") ?: throw IllegalStateException("API Key not found in environment variables")
    val gptClient = GPTClient(apiKey)
    val conversationHandler = ConversationHandler(gptClient)
    conversationHandler.start()
}