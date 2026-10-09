package com.docuchat.docuchat.controller;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VectorStoreTestController {

    private final VectorStore vectorStore;

    public VectorStoreTestController(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @GetMapping("/load-samples")
    public String loadSamples() {
        vectorStore.add(List.of(
                new Document("Employees are entitled to 24 days of annual leave per year."),
                new Document("The office is open from 9 AM to 6 PM, Monday to Friday."),
                new Document("Expense reports must be submitted within 30 days of purchase."),
                new Document("Remote work is allowed up to two days per week with manager approval.")
        ));
        return "Loaded 4 sample documents.";
    }

    @GetMapping("/search")
    public List<String> search(@RequestParam String query) {
        return vectorStore.similaritySearch(SearchRequest.builder().query(query).topK(2).build())
                .stream()
                .map(Document::getText)
                .toList();
    }
}