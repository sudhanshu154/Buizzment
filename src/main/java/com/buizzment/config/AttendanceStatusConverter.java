package com.buizzment.config;

import com.buizzment.model.Attendance;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AttendanceStatusConverter implements Converter<String, Attendance.AttendanceStatus> {
    
    @Override
    public Attendance.AttendanceStatus convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            throw new IllegalArgumentException("Attendance status cannot be null or empty");
        }
        return Attendance.AttendanceStatus.fromCode(source.trim());
    }
} 