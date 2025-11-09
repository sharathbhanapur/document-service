package com.documents.document_service.entity;

public class DocumentVersionBuilder {
    private final DocumentVersion version = new DocumentVersion();

    public DocumentVersionBuilder fromDocument(Document document) {
        version.setContent(document.getContent());
        version.setDocumentId(document.getId());
        version.setTitle(document.getTitle());
        version.setVersionNumber(document.getVersion());
        version.setUpdatedAt(document.getUpdatedAt());
        version.setUpdatedBy(document.getCreatedBy());

        return this;
    }

    public DocumentVersion build() {
        return version;
    }
}
