package com.documents.document_service.exceptions;

public class DocumentNotFoundException extends RuntimeException {
    private String message;
    public DocumentNotFoundException(String message)
    {
        super(message);
        this.message = message;
    }
}
