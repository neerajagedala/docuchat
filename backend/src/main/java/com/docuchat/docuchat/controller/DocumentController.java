package com.docuchat.docuchat.controller;

import com.docuchat.docuchat.service.DocumentIngestionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.docuchat.docuchat.service.DocumentRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.List;

import java.io.IOException;

@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentIngestionService ingestionService;
    private final DocumentRegistry documentRegistry;

    public DocumentController(DocumentIngestionService ingestionService,
                              DocumentRegistry documentRegistry) {
        this.ingestionService = ingestionService;
        this.documentRegistry = documentRegistry;
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        int chunkCount = ingestionService.ingest(file);
        return "Uploaded '" + file.getOriginalFilename() + "' and created " + chunkCount + " chunks.";
    }
    @GetMapping
    public List<String> list() {
        return documentRegistry.list();
    }
}