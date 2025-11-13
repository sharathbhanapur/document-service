package com.documents.document_service.controller;

import com.documents.document_service.entity.Document;
import com.documents.document_service.service.DocumentService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
    controllers = DocumentController.class,
    excludeFilters = @ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = {
            com.documents.document_service.config.AuditorAwareImpl.class,
            com.documents.document_service.config.AuditorConfig.class
        }
    )
)
class DocumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DocumentService documentService;

    @Test
    @DisplayName("should return paged documents")
    void testGetAllDocuments() throws Exception {
        Document doc1 = new Document();
        doc1.setTitle("Doc1");
        Document doc2 = new Document();
        doc2.setTitle("Doc2");

        Mockito.when(documentService.getAllDocuments(any(PageRequest.class)))
                .thenReturn(Arrays.asList(doc1, doc2));

        mockMvc.perform(get("/api/documents?page=0&size=2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Doc1"))
                .andExpect(jsonPath("$[1].title").value("Doc2"));
    }

    @Test
    @DisplayName("should return document by title if present")
    void testGetDocumentByTitle_found() throws Exception {
        Document doc = new Document();
        doc.setTitle("SampleDoc");

        Mockito.when(documentService.getDocumentByTitle(eq("SampleDoc"))).thenReturn(Optional.of(doc));

        mockMvc.perform(get("/api/documents/SampleDoc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.present").value(true))
                .andExpect(jsonPath("$.get.title").value("SampleDoc"));
    }

    @Test
    @DisplayName("should return 200 and Optional.empty() structure if not found")
    void testGetDocumentByTitle_notFound() throws Exception {
        Mockito.when(documentService.getDocumentByTitle(eq("Missing"))).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/documents/Missing"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.present").value(false));
    }

    @Test
    @DisplayName("should create document and return 201")
    void testCreateDocument() throws Exception {
        Document doc = new Document();
        doc.setTitle("NewDoc");
        doc.setContent("Text...");

        Mockito.when(documentService.createDocument(any(Document.class))).thenReturn(doc);

        String json = "{\"title\": \"NewDoc\", \"content\": \"Text...\"}";

        mockMvc.perform(post("/api/documents")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("NewDoc"));
    }

    @Test
    @DisplayName("should delete document and return 204")
    void testDeleteDocument() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.doNothing().when(documentService).deleteDocument(eq(id));

        mockMvc.perform(delete("/api/documents/" + id))
                .andExpect(status().isNoContent());
    }
}
