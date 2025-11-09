package com.documents.document_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "documents")
public class Document {
    //Getter and Setters
    @Setter
    @Getter
    @Id
    @GeneratedValue
    private UUID id;

   @Setter
   @Getter
   private String title;

   @Setter
   @Getter
   @Column(columnDefinition = "TEXT" )
    private String content;

   @Setter
   @Getter
   private UUID createdBy;

   @Setter
   @Getter
   @Column(nullable = false)
   private LocalDateTime createdAt;

   @Setter
   @Getter
   private LocalDateTime updatedAt;

   @Getter
   @Setter
   private UUID updatedBy;

   @Setter
   @Getter
   private boolean isActive = false;

    @Version
    @Setter
    @Getter
    private int version; // used for optimistic locking
    /*Each time a user updates the document, Hibernate checks if the version in the DB matches the one sent by the client.
    If someone else has modified the record in between, a OptimisticLockException is thrown — meaning the user must refresh and retry.*/

}
