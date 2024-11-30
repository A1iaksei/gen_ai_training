package com.epam.training.gen.ai.configuration;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.epam.training.gen.ai.service.SimplePromptService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ClientOpenAiProperties.class)
public class OpenAIConfiguration {
  private final ClientOpenAiProperties clientOpenAiProperties;

  /**
   * Creates an {@link OpenAIAsyncClient} bean for interacting with Azure OpenAI Service asynchronously.
   *
   * @return an instance of {@link OpenAIAsyncClient}
   */
  @Bean
  public OpenAIAsyncClient openAIAsyncClient() {
    return new OpenAIClientBuilder()
        .credential(new AzureKeyCredential(clientOpenAiProperties.clientOpenAiKey()))
        .endpoint(clientOpenAiProperties.clientOpenAiEndpoint())
        .buildAsyncClient();
  }

  /**
   * Creates an {@link SimplePromptService} bean for interacting with REST API and end user.
   *
   * @return an instance of {@link SimplePromptService}
   */
  @Bean
  public SimplePromptService simplePromptService() {
      return new SimplePromptService(openAIAsyncClient(), clientOpenAiProperties.clientOpenAiDeploymentName());
  }
}
