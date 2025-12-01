package com.buizzment.dto;

import com.buizzment.model.AttendanceV2.AttendanceStatus;
import lombok.Data;
import java.time.LocalDate;
import java.util.Map;

@Data
public class AttendanceResponseDto {
    private String id;
    private String workerId;
    private Map<LocalDate, AttendanceStatus> dailyRecords;
    private int presentDays;
    private int absentDays;
}