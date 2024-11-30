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
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ClientOpenAiProperties.class)
public class SemanticKernelConfiguration {
  private final ClientOpenAiProperties clientOpenAiProperties;
  private final OpenAIAsyncClient openAIAsyncClient;

  public static final String PLUGIN_NAME = "Simple plugin";
  public static final double TEMPERATURE = 1.0;

  /**
   * Creates a {@link ChatCompletionService} bean for handling chat completions using Azure OpenAI.
   *
   * @return an instance of {@link ChatCompletionService}
   */
  @Bean
  public ChatCompletionService chatCompletionService() {
    return OpenAIChatCompletion.builder()
        .withModelId(clientOpenAiProperties.clientOpenAiDeploymentName())
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
   * @param kernelPlugin the {@link KernelPlugin} to be used in the kernel
   * @return an instance of {@link Kernel}
   */
  @Bean
  public Kernel kernel(ChatCompletionService chatCompletionService, KernelPlugin kernelPlugin) {
    return Kernel.builder()
        .withAIService(ChatCompletionService.class, chatCompletionService)
        .withPlugin(kernelPlugin())
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
        .withPromptExecutionSettings(PromptExecutionSettings.builder().withTemperature(1.0).build())
        .build();
  }

  /**
   * Creates a map of {@link PromptExecutionSettings} for different models.
   *
   * @return a map of model names to {@link PromptExecutionSettings}
   */
  @Bean
  public Map<String, PromptExecutionSettings> promptExecutionSettingsMap() {
    return Map.of(
        clientOpenAiProperties.clientOpenAiDeploymentName(),
        PromptExecutionSettings.builder().withTemperature(TEMPERATURE).build());
  }
}
