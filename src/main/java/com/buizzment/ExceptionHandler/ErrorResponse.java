package com.buizzment.ExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

// Error response DTO
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private int status;
    private String message;
    private Map<String, String> errors;  // Only for validation errors
}