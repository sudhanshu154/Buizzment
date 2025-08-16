package com.buizzment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyAttendanceReport {
    private String tenderId;
    private YearMonth month;
    private int totalWorkers;
    private long presentDays;
    private long absentDays;
    private long overtimeHours;
}