package com.buizzment.dto;

import com.buizzment.model.Attendance;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BulkAttendanceRequest {
    @NotBlank
    private String tenderId;
    @NotNull
    private LocalDate date;
    private Map<String, Attendance.AttendanceStatus> workerStatuses;  // workerId -> status
}