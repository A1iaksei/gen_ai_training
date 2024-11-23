package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.ChatCompletionsOptions;
import com.azure.ai.openai.models.ChatRequestUserMessage;
import java.util.List;

import com.epam.training.gen.ai.exception.GenAiBadRequestException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class SimplePromptService {
  private final OpenAIAsyncClient aiAsyncClient;
  private final String deploymentOrModelName;

  /**
   * interacts with the Azure OpenAI API to generate chat completions based on user provided
   * message. It retrieves responses from the AI model and logs them.
   *
   * @param message with user prompt
   * @return a {@link List<String>} answers
   */
  public List<String> getChatCompletions(String message) {
    var completions =
        aiAsyncClient
            .getChatCompletions(
                deploymentOrModelName,
                new ChatCompletionsOptions(List.of(new ChatRequestUserMessage(message))))
            .block();

    var messages =
        completions.getChoices().stream()
            .map(
                c -> {
                  var text = c.getMessage().getContent();
                  return text.replaceAll("\n", "");
                })
            .toList();
    log.info(messages.toString());
    return messages;
  }
}
