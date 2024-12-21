package com.epam.training.gen.ai.configuration;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.epam.training.gen.ai.plugin.SimplePlugin;
import com.microsoft.semantickernel.Kernel;
import com.microsoft.semantickernel.aiservices.openai.chatcompletion.OpenAIChatCompletion;
import com.microsoft.semantickernel.orchestration.InvocationContext;
import com.microsoft.semantickernel.orchestration.PromptExecutionSettings;
import com.microsoft.semantickernel.plugin.KernelPlugin;
import com.microsoft.semantickernel.plugin.KernelPluginFactory;
import com.microsoft.semantickernel.services.chatcompletion.ChatCompletionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ClientOpenAiProperties.class)
public class SemanticKernelConfiguration {
  private final ClientOpenAiProperties clientOpenAiProperties;
  private final OpenAIAsyncClient openAIAsyncClient;

  public static final String PLUGIN_NAME = "Simple plugin";

  public PromptProperties getPromptProperties() {
    return clientOpenAiProperties.promptProperties();
  }

  /**
   * Creates a {@link ChatCompletionService} bean for handling chat completions using Azure OpenAI.
   *
   * @return an instance of {@link ChatCompletionService}
   */
  @Bean
  @Scope(value = "prototype")
  public ChatCompletionService chatCompletionService(String deploymentName) {
    return OpenAIChatCompletion.builder()
        .withModelId(deploymentName)
        .withOpenAIAsyncClient(openAIAsyncClient)
        .build();
  }

  /**
   * Creates a {@link KernelPlugin} bean using a simple plugin.
   *
   * @return an instance of {@link KernelPlugin}
   */
  @Bean
  public KernelPlugin kernelPlugin() {
    return KernelPluginFactory.createFromObject(new SimplePlugin(), PLUGIN_NAME);
  }

  /**
   * Creates a {@link Kernel} bean to manage AI services and plugins.
   *
   * @param chatCompletionService the {@link ChatCompletionService} for handling completions
   * @return an instance of {@link Kernel}
   */
  @Bean
  @Scope(value = "prototype")
  public Kernel kernel(ChatCompletionService chatCompletionService) {
    return Kernel.builder()
        .withAIService(ChatCompletionService.class, chatCompletionService)
        .build();
  }

  /**
   * Creates an {@link InvocationContext} bean with default prompt execution settings.
   *
   * @return an instance of {@link InvocationContext}
   */
  @Bean
  public InvocationContext invocationContext() {
    return InvocationContext.builder()
        .withPromptExecutionSettings(
            PromptExecutionSettings.builder()
                .withTemperature(getPromptProperties().temperature())
                .withFrequencyPenalty(getPromptProperties().frequencyPenalty())
                .withMaxTokens(getPromptProperties().maxTokens())
                .build())
        .build();
  }
}
