package com.epam.training.gen.ai.service;

import com.azure.ai.openai.OpenAIAsyncClient;
import com.azure.ai.openai.models.EmbeddingItem;
import com.azure.ai.openai.models.EmbeddingsOptions;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections;
import io.qdrant.client.grpc.JsonWithInt;
import io.qdrant.client.grpc.Points;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static io.qdrant.client.PointIdFactory.id;
import static io.qdrant.client.ValueFactory.value;
import static io.qdrant.client.VectorsFactory.vectors;
import static io.qdrant.client.WithPayloadSelectorFactory.enable;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmbeddingService {
    private final OpenAIAsyncClient openAIAsyncClient;
    private final QdrantClient qdrantClient;

    @Value("${qdrant-model-name}")
    private String modelName;

    @Value("${qdrant-collection-name}")
    private String collectionName;

    public void createCollection() throws ExecutionException, InterruptedException {
        var result = qdrantClient.createCollectionAsync(
                        collectionName,
                        Collections.VectorParams.newBuilder()
                                .setDistance(Collections.Distance.Cosine)
                                .setSize(1536)
                                .build())
                .get();
        log.info("Collection was created: [{}]", result.getResult());
    }

    public List<Points.ScoredPoint> search(String text) throws ExecutionException, InterruptedException {
        var embeddings = retrieveEmbeddings(text);
        List<Float> vectors =
                embeddings.stream()
                        .map(EmbeddingItem::getEmbedding)
                        .flatMap(List::stream)
                        .toList();

        return qdrantClient
                .searchAsync(
                        Points.SearchPoints.newBuilder()
                                .setCollectionName(collectionName)
                                .addAllVector(vectors)
                                .setWithPayload(enable(true))
                                .setLimit(5)
                                .build())
                .get();
    }

    public void persistEmbeddings(String text) throws ExecutionException, InterruptedException {
        var embeddings = this.createEmbeddings(text);
        List<Points.PointStruct> pointStructs = embeddings.stream().map(EmbeddingItem::getEmbedding)
                .map(o -> this.buildPointStruct(o, Map.of("text", value(text)))).toList();

        var updateResult = qdrantClient.upsertAsync(collectionName, pointStructs).get();
    }

    public List<EmbeddingItem> retrieveEmbeddings(String text) {
        return openAIAsyncClient
                .getEmbeddings(modelName, new EmbeddingsOptions(List.of(text)))
                .block()
                .getData();
    }

    private Points.PointStruct buildPointStruct(
            List<Float> points, Map<String, JsonWithInt.Value> payload) {
        return Points.PointStruct.newBuilder()
                .setId(id(UUID.randomUUID()))
                .setVectors(vectors(points))
                .putAllPayload(payload)
                .build();
    }

    public List<EmbeddingItem> createEmbeddings(String text) {
        var embeddings = this.retrieveEmbeddings(text);
        return embeddings;
    }

}
