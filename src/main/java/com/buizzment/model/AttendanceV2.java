package com.buizzment.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Document(collection = "attendancesV2")
@Data
public class AttendanceV2 {
    @Id
    private String id;

    private String workerId;
    private Map<LocalDate, AttendanceStatus> dailyRecords = new HashMap<>();

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
                if (Objects.equals(status.code, code)) {
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
}