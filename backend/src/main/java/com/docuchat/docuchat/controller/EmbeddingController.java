package com.docuchat.docuchat.controller;

import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class EmbeddingController {

    private final EmbeddingModel embeddingModel;

    public EmbeddingController(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    @GetMapping("/embed")
    public String embed(@RequestParam String text) {
        float[] vector = embeddingModel.embed(text);
        return "Dimensions: " + vector.length
                + "\nFirst 5 values: " + Arrays.toString(Arrays.copyOf(vector, 5));
    }

    @GetMapping("/similarity")
    public String similarity(@RequestParam String a, @RequestParam String b) {
        float[] v1 = embeddingModel.embed(a);
        float[] v2 = embeddingModel.embed(b);

        double dot = 0, norm1 = 0, norm2 = 0;
        for (int i = 0; i < v1.length; i++) {
            dot += v1[i] * v2[i];
            norm1 += v1[i] * v1[i];
            norm2 += v2[i] * v2[i];
        }
        double score = dot / (Math.sqrt(norm1) * Math.sqrt(norm2));
        return "Cosine similarity: " + score;
    }
}