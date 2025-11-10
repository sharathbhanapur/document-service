package com.documents.document_service.service.impl;

import com.documents.document_service.entity.Document;
import com.documents.document_service.entity.DocumentVersion;
import com.documents.document_service.entity.DocumentVersionBuilder;
import com.documents.document_service.repository.DocumentRepository;
import com.documents.document_service.repository.DocumentVersionRepository;
import com.documents.document_service.service.DocumentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentServiceImpl implements DocumentService {

    private static final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class);

    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository documentVersionRepository;
    public DocumentServiceImpl(DocumentRepository documentRepository,
                               DocumentVersionRepository documentVersionRepository) {
        this.documentRepository = documentRepository;
        this.documentVersionRepository = documentVersionRepository;
    }

    @Override
    public Document createDocument(Document document) {
        document.setActive(true);
        return documentRepository.save(document);
    }

    @Override
    public Document updateDocument(UUID documentId, String updatedContent, UUID updatedBy) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document ID doesn't exist"));

        DocumentVersion version = new DocumentVersionBuilder()
                .fromDocument(document)
                .build();
        document.setUpdatedAt(LocalDateTime.now());
        document.setContent(updatedContent);
        document.setUpdatedBy(updatedBy);

        Document updatedDocument = documentRepository.save(document);
        log.info("Document {} updated to version {}", updatedDocument.getId(), updatedDocument.getVersion());
        return updatedDocument;
    }

    @Override
    public Optional<Document> getDocumentById(UUID id) {
        return documentRepository.findById(id);
    }

    @Override
    public Optional<Document> getDocumentByTitle(String title) {
        return documentRepository.findByTitle(title);
    }

    @Override
    public List<Document> getAllDocuments() {
        return documentRepository.findAll();
    }

    @Override
    public void deleteDocument(UUID id) {
        Optional<Document> docOpt = documentRepository.findById(id);
        if (docOpt.isPresent()) {
            documentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Document not found with ID: " + id);
        }
    }

    @Override
    public List<DocumentVersion> getDocumentHistory(UUID documentId) {
        return documentVersionRepository.findByDocumentIdOrderByVersionNumberDesc(documentId);
    }

    @Override
    public Document restoreDocumentVersion(UUID documentId, Long versionNumber) {
        return null;
    }
}
