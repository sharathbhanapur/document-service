package com.documents.document_service.repository;

import com.documents.document_service.entity.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(com.documents.document_service.config.TestAuditorAwareConfig.class)
class DocumentRepositoryTest {

    @Autowired
    private DocumentRepository documentRepository;

    @Test
    @DisplayName("should save and retrieve document by title")
    void testSaveAndFindByTitle() {
        Document doc = new Document();
        doc.setTitle("IntegrationTest");
        doc.setContent("Some content");
        doc.setId(UUID.randomUUID());
        doc.setActive(true);
        doc.setVersion(1);

        documentRepository.save(doc);

        Optional<Document> found = documentRepository.findByTitle("IntegrationTest");
        assertThat(found).isPresent();
        assertThat(found.get().getContent()).isEqualTo("Some content");
    }

    @Test
    @DisplayName("should find all documents")
    void testFindAll() {
        Document doc1 = new Document();
        doc1.setTitle("doc1");
        doc1.setContent("a");
        doc1.setId(UUID.randomUUID());
        doc1.setVersion(1);
        Document doc2 = new Document();
        doc2.setTitle("doc2");
        doc2.setContent("b");
        doc2.setId(UUID.randomUUID());
        doc2.setVersion(1);

        documentRepository.save(doc1);
        documentRepository.save(doc2);

        List<Document> result = documentRepository.findAll();
        assertThat(result.size()).isGreaterThanOrEqualTo(2);
    }
}
