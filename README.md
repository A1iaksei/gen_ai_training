## Task 1
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

## Task 2

### MaxTokes

   maxTokens = 10
   ```
   {
    "input": "Who is the first astronaut?",
    "output": "The first astronaut is Yuri Gagarin. He"
   }
   ```

   maxTokens = 50
   ```
   {
    "input": "Who is the first astronaut?",
    "output": "The first astronaut is Yuri Gagarin. He became the first human to travel into space on April 12, 1961, aboard the Vostok 1 spacecraft."
   }
   ```

   ---
### Temperature

   temperature = 0.1 (low creativity) 
   ```
   {
    "input": "come up with the first paragraph of a story about a girl named Lisa who went for a walk and got lost in the forest",
    "output": "Lisa had always been an adventurous soul, constantly seeking new experiences and uncharted territories. On a crisp autumn morning, with the sun casting its golden rays through the dense foliage, she decided to embark on a solitary walk in the enchanting forest that bordered her small town. With a backpack slung over her shoulder and a heart brimming with excitement, little did Lisa know that this seemingly innocent stroll would soon lead her into a labyrinth of towering trees, where every step she took would only deepen her ent"
   }
   ```

   temperature = 1 (high creativity)
   ```
   {
   "input": "come up with the first paragraph of a story about a girl named Lisa who went for a walk and got lost in the forest",
   "output": "The air was thick with anticipation as Lisa began her leisurely stroll through the lush woodland that cradled her small village. With each step, the weight of her mundane worries effortlessly dissipated, replaced by the alluring enchantment of nature's symphony. The sun's gentle rays streamed through the emerald canopy overhead, casting dancing shadows on the forest floor. Lisa reveled in the harmony of chirping birds, rustling leaves, and distant whispers of a playful breeze. Unbeknownst"
   }
   ```

   ---
### Chat with history
   
   * first response:
   ```
   "input": "What is an aspirin?",
    "output": "Aspirin is a common over-the-counter medication that belongs to the class of drugs known as nonsteroidal anti-inflammatory drugs (NSAIDs). Its active ingredient is acetylsalicylic acid, which helps to reduce pain, fever,"
   ```

   * second response:
   ```
   {
    "input": "Where can I buy it?",
    "output": "Aspirin is widely available and can be purchased at various places, including pharmacies, drugstores, supermarkets, and online retailers. It is an over-the-counter medication, so you do not need a prescription to buy it. However, it is always"
   }
   ```

## Task 3

### Use Mixtral-8x7B-Instruct-v0.1
* request:
   ```
   {
   "input" : "Glory to the Emperor of Humanity?",
   "model" : "Mixtral-8x7B-Instruct-v0.1"      
   }
   ```
* response:
  ```
  {
    "input": "Glory to the Emperor of Humanity?",
    "output": " The phrase \"Glory to the Emperor of Humanity\" is a quote from the Warhammer 40,000 universe, a popular science fiction franchise. In this fictional universe, the Emperor of Mankind is a powerful and influential figure who rules over humanity with the aid of his Space Marine armies. The phrase is often used as a rallying cry or expression of loyalty by the franchise's many fans.\n\nHowever, it's important to note that",
    "model": "Mixtral-8x7B-Instruct-v0.1"
   }
  ```

### Use Gpt-4o
* request:
   ```
   {
   "input" : "Glory to the Emperor of Humanity?",
   "model" : "gpt-4o"      
   }
   ```
* response:
  ```
   {
   "input": "Glory to the Emperor of Humanity?",
   "output": "\"Glory to the Emperor of Humanity\" is indeed a phrase from the Warhammer 40,000 universe. In this setting, the Emperor of Mankind is a central figure who is revered as a god-like being by the Imperium of Man. The Imperium is a vast, authoritarian regime that spans the galaxy, and its citizens and soldiers often express their devotion and loyalty with such phrases. The Emperor is seen as humanity's greatest protector against numerous threats in this grimdark future, including alien",
   "model": "gpt-4o"
   }
  ```

## Task 4
### Turn on the Porch light

1. ````
    {
    "input": "give me state of all lights",
    "output": "Here are the current states of all the lights:\n\n1. **Table Lamp**: Off\n2. **Porch Light**: Off\n3. **Chandelier**: On",
    "model": "gpt-4o"
    }
    ````
   
2. ````
    {
    "input": "turn on the devise with id 2",
    "output": "The device with ID 2, which is the \"Porch light,\" was successfully turned off and then turned back on.",
    "model": "gpt-4o"
    }
    ````
   
3.  ````
    {
    "input": "give me state of all lights",
    "output": "Here are the current states of all the lights:\n\n1. **Table Lamp**: Off\n2. **Porch light**: On\n3. **Chandelier**: On\n\nIf you would like to make any changes, please let me know!",
    "model": "gpt-4o"
    }
    ````  

## Task 5
### Requirements
For this task you need to install Qdrant in Docker and start it:
  ```
    docker run -p 6333:6333 -p 6334:6334 -v $(pwd)/qdrant_storage qdrant/qdrant
  ``` 