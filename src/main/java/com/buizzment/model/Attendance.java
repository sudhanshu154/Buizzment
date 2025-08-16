package com.buizzment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

@Data
@Document(collection = "attendances")
@CompoundIndexes({
        @CompoundIndex(name = "tender_worker_month",
                def = "{'tender_id': 1, 'worker_id': 1, 'month_year': 1}",
                unique = true),
        @CompoundIndex(name = "worker_month",
                def = "{'worker_id': 1, 'month_year': 1}")
})
public class Attendance {
    @Id
    private String id;

    @NotBlank
    @Field("tender_id")
    private String tenderId;

    @NotBlank
    @Field("worker_id")
    private String workerId;

    @Field("worker_name")
    private String workerName;

    @Field("month_year")
    private YearMonth monthYear;

    @Field("daily_records")
    private Map<LocalDate, AttendanceStatus> dailyRecords = new HashMap<>();

    @Field("summary")
    private AttendanceSummary summary;

    @Field("last_updated")
    private LocalDateTime lastUpdated = LocalDateTime.now();

    public enum AttendanceStatus {
        PRESENT("P"),
        ABSENT("A"),
        OFF_DAY("O");

        private final String code;

        AttendanceStatus(String code) {
            this.code = code;
        }


        public static AttendanceStatus fromCode(String code) {
            for (AttendanceStatus status : values()) {
                if (status.code == code) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Invalid attendance status code: " + code);
        }


        @JsonValue
        public String getCode() {
            return String.valueOf(code);
        }
        @JsonCreator
        public static AttendanceStatus fromValue(String value) {
            for (AttendanceStatus status : AttendanceStatus.values()) {
                if (status.name().equalsIgnoreCase(value) || status.code.equalsIgnoreCase(value)) {
                    return status;
                }
            }
            throw new IllegalArgumentException("Unknown attendance status: " + value);
        }
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AttendanceSummary {
        private int presentDays;
        private int absentDays;
//        private int offDays;
//        private int halfDays;
//        private int leaveDays;
//        private int overtimeHours;

        public void calculateFromDailyRecords(Map<LocalDate, AttendanceStatus> records) {
            this.presentDays = (int) records.values().stream()
                    .filter(s -> s == AttendanceStatus.PRESENT)
                    .count();

            this.absentDays = (int) records.values().stream()
                    .filter(s -> s == AttendanceStatus.ABSENT)
                    .count();

            // Similar for other statuses
        }
    }
}