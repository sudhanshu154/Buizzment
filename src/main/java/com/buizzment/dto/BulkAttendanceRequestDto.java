package com.buizzment.dto;

import com.buizzment.model.AttendanceV2.AttendanceStatus;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Map;

@Data
public class BulkAttendanceRequestDto {
    @NotBlank
    private String tenderId;

    @NotNull
    private Map<String, Map<LocalDate, AttendanceStatus>> workerAttendance;
    // workerId -> {date -> status}

    @NotNull
    private String attendanceSheetId;
}