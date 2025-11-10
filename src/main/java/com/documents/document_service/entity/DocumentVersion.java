package com.documents.document_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.UUID;
@Entity
@Table(name = "document_versions")
@Slf4j
public class DocumentVersion {

    @Id
    @GeneratedValue
    @Setter
    @Getter
    private UUID id;


    @Column(nullable = false)
    @Setter
    @Getter
    private UUID documentId;

    @Column(nullable = false)
    @Setter
    @Getter
    private int versionNumber;

    @Setter
    @Getter
    private String title;

    @Column(columnDefinition = "TEXT")
    @Setter
    @Getter
    private String content;

    @Setter
    @Getter
    private UUID updatedBy;

    @Setter
    @Getter
    private LocalDateTime updatedAt;

}
