package com.epam.training.gen.ai.controller;

import com.epam.training.gen.ai.model.EmbeddingRequest;
import com.epam.training.gen.ai.model.EmbeddingResponse;
import com.epam.training.gen.ai.service.RagService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("rag")
@RequiredArgsConstructor
public class RagController {

    private final RagService ragService;

    @PostMapping(value = "/documents")
    public ResponseEntity<String> addDocument(@RequestBody String text) {
        ragService.store(text);
        return ResponseEntity.status(HttpStatus.CREATED).body("Document added");
    }

    @PostMapping(value = "/prompt")
    public ResponseEntity<List<EmbeddingResponse>> searchRag(
            @RequestBody String prompt) {
        List<EmbeddingResponse> response = ragService.search(prompt);
        return ResponseEntity.ok(response);
    }

}
