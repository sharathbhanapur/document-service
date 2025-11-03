package com.documents.document_service.service;

import com.documents.document_service.entity.Document;
import com.documents.document_service.exceptions.DocumentNotFoundException;
import com.documents.document_service.repository.DocumentRepository;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;
    private final Path fileStorageLocation = Paths.get("uploaded-files").toAbsolutePath().normalize();

    /**
 * Constructs a new instance of DocumentService with the given DocumentRepository.
 *
 * @param documentRepository the repository used to interact with documents in the database
 */
public DocumentService(DocumentRepository documentRepository) throws IOException {
    this.documentRepository = documentRepository;
    Files.createDirectories(fileStorageLocation);
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

    public Document storeFile(MultipartFile file, UUID uploadedBy) {
        try {
            String fileName = StringUtils.cleanPath(file.getOriginalFilename());
            Path targetLocation = fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            Document document = new Document();
            document.setFileName(fileName);
            document.setPath(targetLocation.toString());
            document.setSize(file.getSize());
            document.setCreatedBy(uploadedBy);
            document.setCreatedAt(LocalDateTime.now());

            return documentRepository.save(document);
        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + file.getOriginalFilename(), e);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        try {
            Path filePath = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new RuntimeException("File not found: " + fileName);
            }
        } catch (MalformedURLException e) {
            throw new RuntimeException("File not found: " + fileName, e);
        }
    }
}
