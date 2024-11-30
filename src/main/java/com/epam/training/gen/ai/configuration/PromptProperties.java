package com.epam.training.gen.ai.configuration;

/**
 * Ensure storaging prompt related settings
 *
 * @param temperature Ensures response creativity values from 0.0 to 1.0
 * @param frequencyPenalty Reduces the probability of words that have already been generated, values
 *     from 0.0 to 1.0
 * @param maxTokens Maximum tokens amount
 */
public record PromptProperties(double temperature, double frequencyPenalty, int maxTokens) {}
