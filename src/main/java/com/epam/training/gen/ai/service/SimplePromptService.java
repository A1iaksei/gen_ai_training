package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.model.AppMessage;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import com.microsoft.semantickernel.services.chatcompletion.ChatMessageContent;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Slf4j
@Service
public abstract class SimplePromptService {
  private final InvocationContext invocationContext;

  @Lookup
  protected abstract ChatCompletionService getChatCompletionService(String deploymentName);

  @Lookup
  protected abstract Kernel getKernel(ChatCompletionService chatCompletionService);

  /**
   * Interacts with the Azure OpenAI API to generate chat completions based on user provided
   * message. It retrieves responses from the AI model and logs them.
   *
   * @param message with user prompt
   * @return a {@link String} answer
   */
  public String getChatCompletions(AppMessage message) {
    var chatHistory = new ChatHistory();
    chatHistory.addUserMessage(message.getInput());

    ChatCompletionService chatCompletionService = getChatCompletionService(message.getModel());
    var response =
        chatCompletionService
            .getChatMessageContentsAsync(chatHistory, getKernel(chatCompletionService), invocationContext)
            .block();

    var answer =
        response.stream().map(ChatMessageContent::getContent).collect(Collectors.joining("\n"));
    log.info(answer);

    return answer;
  }
}
