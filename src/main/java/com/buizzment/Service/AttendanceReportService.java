package com.buizzment.Service;

import com.buizzment.dto.MonthlyAttendanceReport;
import com.buizzment.model.Attendance;
import com.buizzment.repository.AttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AttendanceReportService {
    private final AttendanceRepository attendanceRepository;

    public MonthlyAttendanceReport generateMonthlyReport(
            String tenderId, YearMonth month) {
        List<Attendance> records = attendanceRepository
                .findByTenderIdAndMonthYear(tenderId, month);

        return MonthlyAttendanceReport.builder()
                .tenderId(tenderId)
                .month(month)
                .totalWorkers(records.size())
                .presentDays(calculateTotal(records, Attendance.AttendanceStatus.PRESENT))
                .absentDays(calculateTotal(records, Attendance.AttendanceStatus.ABSENT))
                .build();
    }

    private long calculateTotal(List<Attendance> records, Attendance.AttendanceStatus status) {
        return records.stream()
                .mapToLong(a -> a.getDailyRecords().values()
                        .stream()
                        .filter(s -> s == status)
                        .count())
                .sum();
    }
}