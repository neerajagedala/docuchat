package com.docuchat.docuchat.controller;

import com.docuchat.docuchat.dto.AskResponse;
import com.docuchat.docuchat.service.RagService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RagController {

    private final RagService ragService;

    public RagController(RagService ragService) {
        this.ragService = ragService;
    }

    @GetMapping("/ask")
    public AskResponse ask(@RequestParam String question) {
        return ragService.ask(question);
    }
}