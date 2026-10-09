package com.docuchat.docuchat.service;

import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Service
public class DocumentRegistry {

    private static final Path REGISTRY_FILE = Path.of("data/documents.txt");

    private final Set<String> names = new LinkedHashSet<>();

    public DocumentRegistry() {
        try {
            if (Files.exists(REGISTRY_FILE)) {
                Files.readAllLines(REGISTRY_FILE).stream()
                        .filter(line -> !line.isBlank())
                        .forEach(names::add);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public synchronized void add(String fileName) throws IOException {
        if (names.add(fileName)) {
            Files.createDirectories(REGISTRY_FILE.getParent());
            Files.write(REGISTRY_FILE, names);
        }
    }

    public synchronized List<String> list() {
        return List.copyOf(names);
    }
}