package com.epam.training.gen.ai.controller;

import com.azure.ai.openai.models.EmbeddingItem;
import com.epam.training.gen.ai.model.EmbeddingRequest;
import com.epam.training.gen.ai.model.EmbeddingResponse;
import com.epam.training.gen.ai.service.EmbeddingService;
import io.qdrant.client.grpc.Points;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("embeddings")
@RequiredArgsConstructor
public class EmbeddingController {

    private final EmbeddingService embeddingService;

    /**
     * Endpoint to create a new Qdrant collection.
     *
     */
    @PostMapping(value = "/init")
    public ResponseEntity<String> create() throws ExecutionException, InterruptedException {
        embeddingService.createCollection();
        return ResponseEntity.status(HttpStatus.CREATED).body("Collection created");
    }

    @PostMapping()
    public ResponseEntity<String> saveEmbeddings(@RequestBody EmbeddingRequest request)
            throws ExecutionException, InterruptedException {
        embeddingService.persistEmbeddings(request.text());
        return ResponseEntity.status(HttpStatus.OK).body("Persisted");
    }

    @PostMapping(value = "/preview")
    public ResponseEntity<List<EmbeddingItem>> getEmbeddings(@RequestBody EmbeddingRequest request)
            throws ExecutionException, InterruptedException {
        var embeddings = embeddingService.retrieveEmbeddings(request.text());
        return ResponseEntity.status(HttpStatus.OK).body(embeddings);
    }

    @GetMapping()
    public ResponseEntity<List<EmbeddingResponse>> search(@RequestParam String text)
            throws ExecutionException, InterruptedException {
        var scoredPoints = embeddingService.search(text);
        var response = mapScoredPointsToSearchItem(scoredPoints);

        return ResponseEntity.ok(response);
    }


    private List<EmbeddingResponse> mapScoredPointsToSearchItem(List<Points.ScoredPoint> scoredPoints) {
        List<EmbeddingResponse> embeddingResponseList = new ArrayList<>();
        scoredPoints.forEach(scoredPoint -> {
            var id = scoredPoint.getId().getUuid();
            var score = scoredPoint.getScore();
            var payload = scoredPoint.getPayloadOrDefault("text", null);
            var payloadString = payload.getStringValue();

            var item = EmbeddingResponse.builder().id(id).score(score).payload(payloadString).build();
            embeddingResponseList.add(item);
        });

        return embeddingResponseList;
    }
}
