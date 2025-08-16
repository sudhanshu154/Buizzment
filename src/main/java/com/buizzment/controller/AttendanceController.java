package com.buizzment.controller;

import com.buizzment.Service.AttendanceReportService;
import com.buizzment.dto.BulkAttendanceRequest;
import com.buizzment.dto.MonthlyAttendanceReport;
import com.buizzment.dto.WorkerAttendanceHistory;
import com.buizzment.model.Attendance;
import com.buizzment.Service.AttendanceService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequestMapping("/api/attendances")
@RequiredArgsConstructor
public class AttendanceController {
    private final AttendanceService attendanceService;
    private final AttendanceReportService attendanceReportService;

    @PostMapping
    public ResponseEntity<Void> recordAttendance(
            @RequestParam @NotBlank String tenderId,
            @RequestParam @NotBlank String workerId,
            @RequestParam @NotNull @PastOrPresent LocalDate date,
            @RequestParam Attendance.AttendanceStatus status) {

        attendanceService.recordAttendance(tenderId, workerId, date, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<Attendance> getAttendance(
            @RequestParam String tenderId,
            @RequestParam String workerId,
            @RequestParam LocalDate date,
            @RequestParam Attendance.AttendanceStatus status) {

        Attendance attendance = attendanceService.getAttendance(tenderId, workerId, date, status);
        return ResponseEntity.ok(attendance);
    }

    @PostMapping("/bulk")
    public ResponseEntity<Void> recordBulkAttendance(
            @Valid @RequestBody BulkAttendanceRequest request) {
        attendanceService.recordBulkAttendance(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reports/monthly")
    public ResponseEntity<MonthlyAttendanceReport> getMonthlyReport(
            @RequestParam String tenderId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
        return ResponseEntity.ok(
                attendanceReportService.generateMonthlyReport(tenderId, month)
        );
    }

    @GetMapping("/workers/{workerId}/history")
    public ResponseEntity<WorkerAttendanceHistory> getWorkerHistory(
            @PathVariable String workerId,
            @RequestParam(defaultValue = "6") int months) {
        return ResponseEntity.ok(
                attendanceService.getWorkerHistory(workerId, months)
        );
    }
}