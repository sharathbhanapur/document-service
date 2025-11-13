package com.documents.document_service.service.impl;

import com.documents.document_service.entity.Document;
import com.documents.document_service.entity.DocumentVersion;
import com.documents.document_service.repository.DocumentRepository;
import com.documents.document_service.repository.DocumentVersionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class DocumentServiceImplTest {

    @Mock
    private DocumentRepository documentRepository;

    @Mock
    private DocumentVersionRepository documentVersionRepository;

    @InjectMocks
    private DocumentServiceImpl documentService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateDocument() {
        Document document = new Document();
        document.setTitle("Test");
        document.setContent("Sample content");

        Document saved = new Document();
        saved.setId(UUID.randomUUID());
        saved.setTitle("Test");
        saved.setContent("Sample content");
        saved.setActive(true);
        saved.setCreatedAt(LocalDateTime.now());

        when(documentRepository.save(any(Document.class))).thenReturn(saved);

        Document result = documentService.createDocument(document);

        assertThat(result).isNotNull();
        assertThat(result.getTitle()).isEqualTo("Test");
        assertThat(result.isActive()).isTrue();
        verify(documentRepository).save(any(Document.class));
    }

    @Test
    void testGetAllDocuments_WithPageable() {
        List<Document> docs = Arrays.asList(
                new Document(),
                new Document()
        );
        Page<Document> page = new PageImpl<>(docs);

        when(documentRepository.findAll(any(PageRequest.class))).thenReturn(page);

        List<Document> allDocs = documentService.getAllDocuments(PageRequest.of(0, 2));
        assertThat(allDocs).hasSize(2);
    }

    @Test
    void testGetDocumentById_found() {
        UUID id = UUID.randomUUID();
        Document doc = new Document();
        doc.setId(id);

        when(documentRepository.findById(id)).thenReturn(Optional.of(doc));

        Optional<Document> result = documentService.getDocumentById(id);
        assertThat(result).isPresent().contains(doc);
    }

    @Test
    void testGetDocumentById_notFound() {
        UUID id = UUID.randomUUID();
        when(documentRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Document> result = documentService.getDocumentById(id);
        assertThat(result).isEmpty();
    }

    @Test
    void testUpdateDocument_success() {
        UUID id = UUID.randomUUID();
        Document doc = new Document();
        doc.setId(id);
        doc.setContent("old content");
        doc.setVersion(1);

        when(documentRepository.findById(id)).thenReturn(Optional.of(doc));
        when(documentRepository.save(any(Document.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UUID updatedBy = UUID.randomUUID();

        Document updated = documentService.updateDocument(id, "new content", updatedBy);

        assertThat(updated.getContent()).isEqualTo("new content");
        assertThat(updated.getUpdatedBy()).isEqualTo(updatedBy);
        verify(documentRepository).save(any(Document.class));
    }

    @Test
    void testDeleteDocument_found() {
        UUID id = UUID.randomUUID();
        Document doc = new Document();
        doc.setId(id);

        when(documentRepository.findById(id)).thenReturn(Optional.of(doc));
        doNothing().when(documentRepository).deleteById(id);

        documentService.deleteDocument(id);

        verify(documentRepository).deleteById(id);
    }

    @Test
    void testDeleteDocument_notFound_throws() {
        UUID id = UUID.randomUUID();
        when(documentRepository.findById(id)).thenReturn(Optional.empty());

        org.junit.jupiter.api.Assertions.assertThrows(
            RuntimeException.class,
            () -> documentService.deleteDocument(id)
        );
    }

    @Test
    void testGetDocumentByTitle_found() {
        String title = "DocTitle";
        Document doc = new Document();
        doc.setTitle(title);
        when(documentRepository.findByTitle(title)).thenReturn(Optional.of(doc));

        Optional<Document> result = documentService.getDocumentByTitle(title);
        assertThat(result).isPresent().contains(doc);
    }

    @Test
    void testGetDocumentByTitle_notFound() {
        String title = "Nonexistent";
        when(documentRepository.findByTitle(title)).thenReturn(Optional.empty());

        Optional<Document> result = documentService.getDocumentByTitle(title);
        assertThat(result).isEmpty();
    }

    @Test
    void testGetDocumentHistory() {
        UUID docId = UUID.randomUUID();
        DocumentVersion v1 = new DocumentVersion();
        DocumentVersion v2 = new DocumentVersion();
        when(documentVersionRepository.findByDocumentIdOrderByVersionNumberDesc(docId))
                .thenReturn(Arrays.asList(v1, v2));

        List<DocumentVersion> versions = documentService.getDocumentHistory(docId);

        assertThat(versions).hasSize(2);
    }
}
