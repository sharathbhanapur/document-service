package com.documents.document_service.service.impl;

import com.documents.document_service.entity.Document;
import com.documents.document_service.entity.DocumentVersion;
import com.documents.document_service.entity.DocumentVersionBuilder;
import com.documents.document_service.exceptions.DocumentNotFoundException;
import com.documents.document_service.repository.DocumentRepository;
import com.documents.document_service.repository.DocumentVersionRepository;
import com.documents.document_service.service.DocumentService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class DocumentServiceImpl implements DocumentService {
    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository documentVersionRepository;
    private final Path fileStorageLocation = Paths.get("uploaded-files").toAbsolutePath().normalize();

    /**
 * Constructs a new instance of DocumentService with the given DocumentRepository.
 *
 * @param documentRepository the repository used to interact with documents in the database
 */
public DocumentServiceImpl(DocumentRepository documentRepository,
                           DocumentVersionRepository documentVersionRepository) throws IOException {
    this.documentRepository = documentRepository;
    this.documentVersionRepository = documentVersionRepository;
}

public Document createDocument(Document document) {
    document.setActive(true);
    return documentRepository.save(document);
}

        /**
     * Saves a given document to the database. The document's creation timestamp is
     * automatically set to the current date and time before saving.
     *
     * @param document the document to be saved
     * @return the saved document with its ID and creation timestamp populated
     */
    public Document saveDocument(Document document) {
        document.setCreatedAt(LocalDateTime.now());
        return documentRepository.save(document);
    }
    /**
     * Retrieves a document from the database based on its name. If no document is
     * found with the given name, a DocumentNotFoundException is thrown.
     *
     * @param name the name of the document to be retrieved
     * @return the document with the given name
     * @throws DocumentNotFoundException if no document is found with the given name
     */
    public Document getDocumentByName(String name) {
        return documentRepository.findByName(name)
                .orElseThrow(() -> new DocumentNotFoundException("Document Not Found With Name:" + name));
    }

    // Get all documents uploaded by a user
    public List<Document> getDocumentsByUploader(String uploadedBy) {
        return documentRepository.findByUploadedBy(uploadedBy);
    }

    // Delete document by ID safely
    public void deleteDocument(UUID id) {
        Optional<Document> docOpt = documentRepository.findById(id);
        if (docOpt.isPresent()) {
            documentRepository.deleteById(id);
        } else {
            throw new RuntimeException("Document not found with ID: " + id);
        }
    }

    public List<Document> getAllDocuments()
    {
        return documentRepository.findAll();
    }

    @Override
    public List<DocumentVersion> getDocumentHistory(UUID documentId) {
        return List.of();
    }

    @Override
    public Document restoreDocumentVersion(UUID documentId, Long versionNumber) {
        return null;
    }

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
    public Document getDocumentById(UUID id) {
        return null;
    }

}
