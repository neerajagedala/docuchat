package com.docuchat.docuchat.service;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.docuchat.docuchat.config.VectorStoreConfig;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import java.io.File;

import java.io.IOException;
import java.util.List;

@Service
public class DocumentIngestionService {

    private final VectorStore vectorStore;
    private final DocumentRegistry documentRegistry;
    private final TokenTextSplitter splitter = TokenTextSplitter.builder().build();

    public DocumentIngestionService(VectorStore vectorStore, DocumentRegistry documentRegistry) {
        this.vectorStore = vectorStore;
        this.documentRegistry = documentRegistry;
    }

    public int ingest(MultipartFile file) throws IOException {
        Resource resource = new ByteArrayResource(file.getBytes());

        List<Document> documents = new TikaDocumentReader(resource).get();
        List<Document> chunks = splitter.apply(documents);

        chunks.forEach(chunk ->
                chunk.getMetadata().put("source", file.getOriginalFilename()));

        vectorStore.add(chunks);

        if (vectorStore instanceof SimpleVectorStore simple) {
            File storeFile = new File(VectorStoreConfig.STORE_FILE);
            storeFile.getParentFile().mkdirs();
            simple.save(storeFile);
        }
        documentRegistry.add(file.getOriginalFilename());

        return chunks.size();
    }
}