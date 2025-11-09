package com.documents.document_service.repository;

import com.documents.document_service.entity.Document;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DocumentRepository extends JpaRepository<Document, UUID> {
    Optional<Document> findByName(String name);

    List<Document> findByUploadedBy(String uploadedBy);
}
