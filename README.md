### Project Setup
1. Clone the repository.
2. Edit your application run configurations to add the following environment variables:
   ```
   client-openai-key= <AZUREOPENAI Key>
   client-openai-endpoint= <AZUREOPENAI URL>
   client-openai-deployment-name= <AZUREOPENAI Model>
   ```
3. Run the SpringBoot Application. And use following links:

   - POST http://localhost:8080/chat/semantic-kernel - get answer from LLM with history
   - POST http://localhost:8080/chat/simple - get simple answer from LLM


 Request body example:
   ```
   {
    "input" : "Who are you?"      
   }
   ```

 Response body example:
   ```
   {
    "input": "Who are you?",
    "output": [
        "I am an AI language model developed by OpenAI, How can I help you today?"
    ]
    }
   ```
