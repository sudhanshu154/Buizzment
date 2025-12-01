package com.buizzment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class BulkAttendanceResponseDto {
    private int totalProcessed;
    private int successfulRecords;
    private int failedRecords;
    private List<String> errors;
    private List<String> attendanceIds;

    public BulkAttendanceResponseDto(int totalProcessed, int successfulRecords, int failedRecords) {
        this.totalProcessed = totalProcessed;
        this.successfulRecords = successfulRecords;
        this.failedRecords = failedRecords;
        this.errors = List.of();
    }
}