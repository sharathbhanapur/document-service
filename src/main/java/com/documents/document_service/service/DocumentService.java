package com.documents.document_service.service;

import com.documents.document_service.entity.Document;
import com.documents.document_service.entity.DocumentVersion;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentService {

    Document createDocument(Document document);

    Document updateDocument(UUID id, String updatedContent, UUID updatedBy);

    Optional<Document> getDocumentById(UUID id);

    Optional<Document> getDocumentByTitle(String title);

    List<Document> getAllDocuments();

    void deleteDocument(UUID id);

    List<DocumentVersion> getDocumentHistory(UUID documentId);

    Document restoreDocumentVersion(UUID documentId, Long versionNumber);
}
