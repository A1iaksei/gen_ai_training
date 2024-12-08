package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.model.AppMessage;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.semanticfunctions.KernelFunction;
import com.microsoft.semantickernel.semanticfunctions.KernelFunctionArguments;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.stereotype.Service;

/**
 * Service class for interacting with the AI kernel, maintaining chat history.
 *
 * <p>This service provides a method to process user prompts while preserving chat history. It uses
 * the {@link Kernel} to invoke AI responses based on the user's input and the previous chat
 * context. The conversation history is updated after each interaction.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public abstract class KernelHistoryService {
  private final ChatHistory chatHistory;
  private final InvocationContext invocationContext;

  @Lookup
  protected abstract ChatCompletionService getChatCompletionService(String deploymentName);

  @Lookup
  protected abstract Kernel getKernel(ChatCompletionService chatCompletionService);

  /**
   * Creates the kernel function arguments with the user prompt and chat history.
   *
   * @param prompt the user's input
   * @param chatHistory the current chat history
   * @return a {@link KernelFunctionArguments} instance containing the variables for the AI model
   */
  private KernelFunctionArguments getKernelFunctionArguments(
      String prompt, ChatHistory chatHistory) {
    return KernelFunctionArguments.builder()
        .withVariable("request", prompt)
        .withVariable("chatHistory", chatHistory)
        .build();
  }

  /**
   * Creates a kernel function for generating a chat response using a predefined prompt template.
   * <p>
   * The template includes the chat history and the user's message as variables.
   *
   * @return a {@link KernelFunction} for handling chat-based AI interactions
   */
  private KernelFunction<String> getChat() {
    return KernelFunction.<String>createFromPrompt(
            """
                        {{$chatHistory}}
                        <message role="user">{{$request}}</message>""")
        .build();
  }

  /**
   * process user prompts while preserving chat history.
   * The conversation history is updated after each interaction.
   *
   * @param appMessage the user's input
   * @return a {@link String} LLMs output
   */
  public String processWithHistory(AppMessage appMessage) {

    var response =
        getKernel(getChatCompletionService(appMessage.getModel()))
            .invokeAsync(getChat())
            .withInvocationContext(invocationContext)
            .withArguments(getKernelFunctionArguments(appMessage.getInput(), chatHistory))
            .block();

    chatHistory.addUserMessage(appMessage.getInput());
    chatHistory.addAssistantMessage(response.getResult());
    var result = response.getResult();
    log.info("AI answer:{}", result);

    return result;
  }
}
