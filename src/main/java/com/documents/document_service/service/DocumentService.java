package com.documents.document_service.service;

import com.documents.document_service.entity.Document;
import com.documents.document_service.exceptions.DocumentNotFoundException;
import com.documents.document_service.repository.DocumentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;
/**
 * Constructs a new instance of DocumentService with the given DocumentRepository.
 *
 * @param documentRepository the repository used to interact with documents in the database
 */
public DocumentService(DocumentRepository documentRepository) {
    this.documentRepository = documentRepository;
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
    public void deleteDocument(Long id) {
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

}
