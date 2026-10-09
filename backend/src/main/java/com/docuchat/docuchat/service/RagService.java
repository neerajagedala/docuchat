package com.docuchat.docuchat.service;

import com.docuchat.docuchat.dto.AskResponse;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RagService {

    private final ChatClient chatClient;
    private final VectorStore vectorStore;

    public RagService(ChatClient.Builder builder, VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        this.chatClient = builder
                .defaultSystem("""
        You are DocuChat, a friendly assistant that answers questions about the user's uploaded documents.
        - For greetings or small talk (like "hi" or "thanks"), reply politely and briefly,
          and invite the user to ask about their documents.
        - For any other question, answer using ONLY the provided context.
          If the answer is not in the context, say:
          "I don't know based on the uploaded documents."
        """)
                .build();
    }

    public AskResponse ask(String question) {
        // 1. Retrieve
        List<Document> results = vectorStore.similaritySearch(
                SearchRequest.builder().query(question).topK(3).build());

        // 2. Augment
        String context = results.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));

        String prompt = "Context:\n" + context + "\n\nQuestion: " + question;

        // 3. Generate
        String answer = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        // 4. Collect sources
        List<String> sources = results.stream()
                .map(doc -> String.valueOf(doc.getMetadata().get("source")))
                .distinct()
                .toList();

        return new AskResponse(answer, sources);
    }
}