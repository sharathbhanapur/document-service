package com.documents.document_service.controller;


import com.documents.document_service.entity.Document;
import com.documents.document_service.service.impl.DocumentServiceImpl;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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
    @PostMapping
    public ResponseEntity<Document> uploadDocument(@RequestBody Document document) {
        Document saved = documentService.saveDocument(document);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/{name}")
    public ResponseEntity<Document> getDocumentByName(@RequestBody String name) {
        Document document = documentService.getDocumentByName(name);
        return ResponseEntity.ok(document);
    }

    // 3️⃣ Get Documents by Uploader
    @GetMapping
    public ResponseEntity<List<Document>> getDocumentsByUploader(@RequestParam(required = false) String uploadedBy) {
        if (uploadedBy != null) {
            return ResponseEntity.ok(documentService.getDocumentsByUploader(uploadedBy));
        }
        // If no uploader param, return all documents
        return ResponseEntity.ok(documentService.getAllDocuments());
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
