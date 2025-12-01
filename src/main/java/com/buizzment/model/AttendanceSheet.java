package com.buizzment.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "attendance_sheets")
@Data
@Builder
public class AttendanceSheet {
    @Id
    private String id;

    @Indexed
    private String tenderId;

    @Indexed
    private YearMonth monthYear;

    private String startDate;

    private String endDate;

    private List<String> attendanceIds = new ArrayList<>();

    private LocalDateTime createdAt;
}