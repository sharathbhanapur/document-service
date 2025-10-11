package com.documents.document_service.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "documents")
public class Document {
    @Id
    @GeneratedValue
    private UUID id;

   private String title;

   @Column(columnDefinition = "TEXT" )
    private String content;

   private UUID createdBy;
   private LocalDateTime createdAt;
   private LocalDateTime updatedAt;
   private boolean isActive = false;

   //Getter and Setters
    public UUID getId() {return id;}
    public void setId(UUID id) { this.id = id;}

    public String getTitle() { return title ; }
    public void setTitle(String title) { this.title = title ;}

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public UUID getCreatedBy() { return createdBy; }
    public void setCreatedBy(UUID createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean isActive) { this.isActive = isActive; }
}
