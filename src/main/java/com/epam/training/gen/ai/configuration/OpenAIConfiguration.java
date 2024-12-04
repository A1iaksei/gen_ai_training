package com.epam.training.gen.ai.configuration;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import com.microsoft.semantickernel.services.chatcompletion.ChatHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

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
   * Creates an {@link ChatHistory} bean for collecting conversation history
   *
   * @return an instance of {@link ChatHistory}
   */
  @Bean
  @Scope(value = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
  public ChatHistory chatHistory() {
    return new ChatHistory();
  }
}
