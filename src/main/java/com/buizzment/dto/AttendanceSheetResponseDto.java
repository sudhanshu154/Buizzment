package com.buizzment.dto;

import lombok.Data;
import java.time.YearMonth;
import java.util.List;

@Data
public class AttendanceSheetResponseDto {
    private String id;
    private String tenderId;
    private YearMonth monthYear;
    private List<AttendanceResponseDto> attendances;
    private int totalRecords;
}