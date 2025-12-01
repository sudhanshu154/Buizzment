package com.buizzment.config;

import com.buizzment.model.AttendanceV2;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class AttendanceStatusConverter implements Converter<String, AttendanceV2.AttendanceStatus> {
    
    @Override
    public AttendanceV2.AttendanceStatus convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            throw new IllegalArgumentException("Attendance status cannot be null or empty");
        }
        return AttendanceV2.AttendanceStatus.fromCode(source.trim());
    }

} 