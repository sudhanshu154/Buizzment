package com.buizzment.ExceptionHandler;

// Simple exception class
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
