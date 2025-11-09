package com.documents.document_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/*JPA Auditing will automatically fill in fields like createdAt, updatedAt, and even createdBy / updatedBy (with a little help).
That means  service logic stays clean — no more manually setting timestamps or user IDs.*/
@SpringBootApplication
//The auditorAwareRef tells Spring which bean provides the current user (createdBy / updatedBy).
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class DocumentServiceApplication {
	public static void main(String[] args) {
        SpringApplication.run(DocumentServiceApplication.class, args);
	}

}
