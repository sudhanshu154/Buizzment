package com.buizzment.ExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponse.builder()
                        .message(ex.getMessage())
                        .status(HttpStatus.NOT_FOUND.value())
                        .build());
    }

    @ExceptionHandler({AuthenticationCredentialsNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleAuthException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.builder()
                        .message("Full authentication required by your Handler :) ")
                        .status(HttpStatus.NOT_FOUND.value())
                        .build());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
//        Map<String, String> errors = ex.getBindingResult()
//                .getFieldErrors()
//                .stream()
//                .collect(Collectors.toMap(
//                        FieldError::getField,
//                        FieldError::getDefaultMessage
//                ));

        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage() != null ?
                                fieldError.getDefaultMessage() : "Invalid field value"
                ));
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                        .errors(errors)
                        .message("Validation failed")
                .build());
    }

}