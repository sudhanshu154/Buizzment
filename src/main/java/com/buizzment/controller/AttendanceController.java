//package com.buizzment.controller;
//
//import com.buizzment.Service.AttendanceReportService;
//import com.buizzment.dto.BulkAttendanceRequest;
//import com.buizzment.dto.MonthlyAttendanceReport;
//import com.buizzment.dto.WorkerAttendanceHistory;
//import com.buizzment.model.Attendance;
//import com.buizzment.Service.AttendanceService;
//import jakarta.validation.Valid;
//import jakarta.validation.constraints.NotBlank;
//import jakarta.validation.constraints.NotNull;
//import jakarta.validation.constraints.PastOrPresent;
//import lombok.RequiredArgsConstructor;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.time.LocalDate;
//import java.time.YearMonth;
//
//@RestController
//@RequestMapping("/api/attendances")
//@RequiredArgsConstructor
//public class AttendanceController {
//    private final AttendanceService attendanceService;
//    private final AttendanceReportService attendanceReportService;
//
//    @PostMapping
//    public ResponseEntity<Void> recordAttendance(
//            @RequestParam @NotBlank String tenderId,
//            @RequestParam @NotBlank String workerId,
//            @RequestParam @NotNull @PastOrPresent LocalDate date,
//            @RequestParam Attendance.AttendanceStatus status) {
//        // Records Single worker Attendance
//        attendanceService.recordAttendance(tenderId, workerId, date, status);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping
//    public ResponseEntity<Attendance> getAttendance(
//            @RequestParam String tenderId,
//            @RequestParam String workerId,
//            @RequestParam LocalDate date,
//            @RequestParam Attendance.AttendanceStatus status) {
//        //get record of worker for specific date
//        Attendance attendance = attendanceService.getAttendance(tenderId, workerId, date, status);
//        return ResponseEntity.ok(attendance);
//    }
//
//    @PostMapping("/bulk")
//    public ResponseEntity<Void> recordBulkAttendance(
//            @Valid @RequestBody BulkAttendanceRequest request) {
//        attendanceService.recordBulkAttendance(request);
//        return ResponseEntity.ok().build();
//    }
//
//    @GetMapping("/reports/monthly")
//    public ResponseEntity<MonthlyAttendanceReport> getMonthlyReport(
//            @RequestParam String tenderId,
//            @RequestParam @DateTimeFormat(pattern = "yyyy-MM") YearMonth month) {
//        //get monthly record
//        return ResponseEntity.ok(
//                attendanceReportService.generateMonthlyReport(tenderId, month)
//        );
//    }
//
//    @GetMapping("/workers/{workerId}/history")
//    public ResponseEntity<WorkerAttendanceHistory> getWorkerHistory(
//            @PathVariable String workerId,
//            @RequestParam(defaultValue = "6") int months) {
//        return ResponseEntity.ok(
//                attendanceService.getWorkerHistory(workerId, months)
//        );
//    }
//}


package com.buizzment.controller;

import com.buizzment.Service.AttendanceV2Service;
import com.buizzment.dto.AttendanceResponseDto;
import com.buizzment.dto.AttendanceSheetResponseDto;
import com.buizzment.dto.BulkAttendanceRequestDto;
import com.buizzment.dto.BulkAttendanceResponseDto;
import com.buizzment.model.AttendanceV2;
import com.buizzment.Service.AttendanceService;
import com.buizzment.model.AttendanceSheet;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/attendances")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceV2Service attendanceService;

    @GetMapping("/tender/{tenderId}/month/{monthYear}")
    public ResponseEntity<AttendanceSheetResponseDto> getMonthlyAttendance(
            @PathVariable String tenderId,
            @PathVariable YearMonth monthYear) {

        List<AttendanceV2> attendances = attendanceService.getMonthlyAttendances(tenderId, monthYear);

        AttendanceSheetResponseDto response = new AttendanceSheetResponseDto();
        response.setTenderId(tenderId);
        response.setMonthYear(monthYear);
        response.setAttendances(attendances.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList()));
        response.setTotalRecords(attendances.size());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/")
    public ResponseEntity<AttendanceSheet> createAttendanceSheet(
            @RequestBody AttendanceSheet attendanceSheet){
        return ResponseEntity.ok(attendanceService.createAttendanceSheet(attendanceSheet));
    }

//    @PostMapping("/record")
//    public ResponseEntity<AttendanceV2> recordAttendance(
//            @RequestParam String tenderId,
//            @RequestParam String workerId,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
//            @RequestParam AttendanceV2.AttendanceStatus status) {
//
//        AttendanceV2 attendance = attendanceService.recordAttendance(tenderId, workerId, date, status);
//        return ResponseEntity.ok(attendance);
//    }

    private AttendanceResponseDto convertToDto(AttendanceV2 attendance) {
        AttendanceResponseDto dto = new AttendanceResponseDto();
        dto.setId(attendance.getId());
        dto.setWorkerId(attendance.getWorkerId());
        dto.setDailyRecords(attendance.getDailyRecords());

        // Calculate summary
        dto.setPresentDays((int) attendance.getDailyRecords().values().stream()
                .filter(s -> s == AttendanceV2.AttendanceStatus.PRESENT)
                .count());
        dto.setAbsentDays((int) attendance.getDailyRecords().values().stream()
                .filter(s -> s == AttendanceV2.AttendanceStatus.ABSENT)
                .count());

        return dto;
    }

    @GetMapping("/{tenderId}")
    public ResponseEntity<List<AttendanceSheet>> getAttendaceSheet(@PathVariable String tenderId){
        return ResponseEntity.ok(attendanceService.getAttendanceSheetByTenderId(tenderId));
    }

    @PostMapping("/bulk")
    public ResponseEntity<BulkAttendanceResponseDto> recordBulkAttendance(
            @Valid @RequestBody BulkAttendanceRequestDto request) {

        BulkAttendanceResponseDto response = attendanceService.recordBulkAttendance(request);

        if (response.getFailedRecords() > 0) {
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT).body(response);
        }

        return ResponseEntity.ok(response);
    }

    // Enhanced CSV parser for worker-based processing
    @PostMapping("/bulk/csv")
    public ResponseEntity<BulkAttendanceResponseDto> recordBulkAttendanceFromCsv(
            @RequestParam String tenderId,
            @RequestParam("file") MultipartFile file) {

        try {
            BulkAttendanceRequestDto request = parseCsvToBulkRequest(tenderId, file);
            BulkAttendanceResponseDto response = attendanceService.recordBulkAttendance(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                    new BulkAttendanceResponseDto(0, 0, 1, List.of(e.getMessage()),List.of())
            );
        }
    }

    private BulkAttendanceRequestDto parseCsvToBulkRequest(String tenderId, MultipartFile file)
            throws IOException {
        BulkAttendanceRequestDto request = new BulkAttendanceRequestDto();
        request.setTenderId(tenderId);

        // workerId -> Map<Date, Status>
        Map<String, Map<LocalDate, AttendanceV2.AttendanceStatus>> workerAttendance = new HashMap<>();

        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim())) {

            for (CSVRecord record : csvParser) {
                String workerId = record.get("workerId");
                String dateStr = record.get("date");
                String statusStr = record.get("status");

                try {
                    LocalDate date = LocalDate.parse(dateStr);
                    AttendanceV2.AttendanceStatus status = parseAttendanceStatus(statusStr);

                    // Group by worker
                    workerAttendance
                            .computeIfAbsent(workerId, k -> new HashMap<>())
                            .put(date, status);

                } catch (Exception e) {
                    throw new IOException("Invalid data for worker " + workerId + ": " + e.getMessage());
                }
            }
        }

        request.setWorkerAttendance(workerAttendance);
        return request;
    }
    private AttendanceV2.AttendanceStatus parseAttendanceStatus(String statusStr) {
        if (statusStr == null) throw new IllegalArgumentException("Status cannot be null");

        String upperStatus = statusStr.toUpperCase();
        for (AttendanceV2.AttendanceStatus status : AttendanceV2.AttendanceStatus.values()) {
            if (status.name().equals(upperStatus) ||
                    String.valueOf(status.getCode()).equals(upperStatus)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status: " + statusStr);
    }

    @PostMapping("/remove")
    public ResponseEntity<String> removeWorker(@RequestParam String attendanceId,@RequestParam String sheetId){
        try{
            attendanceService.deleteAttendance(attendanceId,sheetId);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.ok("Somthing went wrong... "+e);
        }
    }

    @PostMapping("/change")
    public ResponseEntity<String> changeWorker(@RequestParam String attendanceId,@RequestParam String workerId){
        try{
            attendanceService.changeWorker(attendanceId,workerId);
            return ResponseEntity.ok("Success");
        } catch (Exception e) {
            return ResponseEntity.ok("Somthing went wrong... "+e);
        }
    }
}