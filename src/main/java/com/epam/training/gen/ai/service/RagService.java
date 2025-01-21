package com.epam.training.gen.ai.service;

import com.epam.training.gen.ai.configuration.ClientOpenAiProperties;
import com.epam.training.gen.ai.model.EmbeddingRequest;
import com.epam.training.gen.ai.model.EmbeddingResponse;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.DocumentSplitters;
import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import dev.langchain4j.store.embedding.EmbeddingMatch;
import dev.langchain4j.store.embedding.EmbeddingSearchRequest;
import dev.langchain4j.store.embedding.EmbeddingSearchResult;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.qdrant.QdrantEmbeddingStore;
import io.qdrant.client.QdrantClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RagService {

    private static final Integer MAX_SEGMENT_SIZE_IN_TOKENS = 100;
    private static final Integer MAX_OVERLAP_SIZE_IN_TOKENS = 0;
    private static final String TOKENIZER_MODEL = "gpt-4";
    private static final String COLLECTION_NAME = "RAG";

    private EmbeddingStore<TextSegment> embeddingStore;
    private EmbeddingModel embeddingModel;

    public RagService(final QdrantClient qdrantClient) {
        this.embeddingStore = QdrantEmbeddingStore.builder()
                .client(qdrantClient)
                .collectionName(COLLECTION_NAME)
                .build();
        this.embeddingModel = new AllMiniLmL6V2EmbeddingModel();
    }

    public void store(String text) {
        List<TextSegment> segments = segment(text);
        List<Embedding> embeddings = embeddingModel.embedAll(segments).content();
        embeddingStore.addAll(embeddings, segments);
    }

    private List<TextSegment> segment(String text) {
        Document document = Document.document(text);
        DocumentSplitter splitter = DocumentSplitters.recursive(
                MAX_SEGMENT_SIZE_IN_TOKENS,
                MAX_OVERLAP_SIZE_IN_TOKENS,
                new OpenAiTokenizer(TOKENIZER_MODEL));
        return splitter.split(document);
    }
    public List<EmbeddingResponse> search(String prompt) {
        var queryEmbedding = embeddingModel.embed(prompt).content();
        EmbeddingSearchRequest embeddingSearchRequest = new EmbeddingSearchRequest(queryEmbedding, 1, 0.5, null);

        EmbeddingSearchResult result = embeddingStore.search(embeddingSearchRequest);
        List<EmbeddingMatch> matches = result.matches();
        return matches.stream()
                .map(embeddingMatch -> {
                    TextSegment textSegment = (TextSegment) embeddingMatch.embedded();
                    return new EmbeddingResponse(embeddingMatch.embeddingId(),
                            embeddingMatch.score().floatValue(),
                            textSegment.text());
                })
                .toList();
    }



}
