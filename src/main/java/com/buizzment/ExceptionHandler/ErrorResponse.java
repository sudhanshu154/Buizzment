package com.buizzment.ExceptionHandler;

import lombok.AllArgsConstructor;
import lombok.Data;

// Error response DTO
@Data
@AllArgsConstructor
public class ErrorResponse {
    private int status;
    private String message;
}
