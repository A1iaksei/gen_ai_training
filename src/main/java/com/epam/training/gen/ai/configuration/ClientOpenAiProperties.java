package com.epam.training.gen.ai.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties()
public record ClientOpenAiProperties(
    String clientOpenAiKey,
    String clientOpenAiEndpoint,
    String clientOpenAiDeploymentName,
    PromptProperties promptProperties) {}
