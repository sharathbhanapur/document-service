package com.documents.document_service.controller;


import com.documents.document_service.entity.Document;
import com.documents.document_service.service.impl.DocumentServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private static final Logger logger = LoggerFactory.getLogger(DocumentController.class);

    private final DocumentServiceImpl documentService;

    public DocumentController(DocumentServiceImpl documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/{title}")
    public ResponseEntity<Optional<Document>> getDocumentByTitle(@PathVariable String title) {
        Optional<Document> document = documentService.getDocumentByTitle(title);
        return ResponseEntity.ok(document);
    }

    // 4️⃣ Delete Document by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable UUID id) {
        logger.warn("Deleting document with ID: {}", id);
        documentService.deleteDocument(id);
        logger.info("Document with ID: {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }

}
