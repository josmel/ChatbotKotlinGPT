fun main() {
    val apiKey = "YOUR_API_KEY"
    val gptClient = GPTClient(apiKey)
    val conversationHandler = ConversationHandler(gptClient)
    conversationHandler.start()
}