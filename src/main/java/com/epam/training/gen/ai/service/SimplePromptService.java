package com.epam.training.gen.ai.service;

import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public class SimplePromptService {
  private final ChatCompletionService chatCompletionService;
  private final Kernel kernel;
  private final InvocationContext invocationContext;

  /**
   * Interacts with the Azure OpenAI API to generate chat completions based on user provided
   * message. It retrieves responses from the AI model and logs them.
   *
   * @param message with user prompt
   * @return a {@link String} answer
   */
  public String getChatCompletions(String message) {
    var chatHistory = new ChatHistory();
    chatHistory.addUserMessage(message);

    var response =
        chatCompletionService
            .getChatMessageContentsAsync(chatHistory, kernel, invocationContext)
            .block();

    var answer =
        response.stream().map(ChatMessageContent::getContent).collect(Collectors.joining("\n"));
    log.info(answer);

    return answer;
  }
}
