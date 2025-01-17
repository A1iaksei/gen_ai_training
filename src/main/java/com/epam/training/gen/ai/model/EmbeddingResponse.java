package com.epam.training.gen.ai.model;

import lombok.Builder;

@Builder
public record EmbeddingResponse(String id, float score, String payload) {
}
