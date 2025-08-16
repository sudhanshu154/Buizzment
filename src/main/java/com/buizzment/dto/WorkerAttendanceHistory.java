package com.buizzment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;
import java.util.IntSummaryStatistics;
import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkerAttendanceHistory {
    private String workerId;
    private Map<YearMonth, IntSummaryStatistics> attendanceByMonth;
}