package com.buizzment.Service;

import com.buizzment.dto.BulkAttendanceRequest;
import com.buizzment.dto.WorkerAttendanceHistory;
import com.buizzment.model.Attendance;
import com.buizzment.model.Worker;
import com.buizzment.repository.AttendanceRepository;
import com.buizzment.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {
    private final AttendanceRepository attendanceRepository;
    private final WorkerRepository workerRepository;

    public void recordAttendance(String tenderId, String workerId, LocalDate date,
                                 Attendance.AttendanceStatus status) {
        YearMonth monthYear = YearMonth.from(date);

        Attendance attendance = attendanceRepository
                .findByTenderIdAndWorkerIdAndMonthYear(tenderId, workerId, monthYear)
                .orElseGet(() -> createNewAttendanceRecord(tenderId, workerId, monthYear));

        attendance.getDailyRecords().put(date, status);
        attendance.getSummary().calculateFromDailyRecords(attendance.getDailyRecords());
        attendanceRepository.save(attendance);
    }

    private Attendance createNewAttendanceRecord(String tenderId, String workerId, YearMonth monthYear) {
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new RuntimeException("Worker not found"));

        //TODO worker's tenderIds should contain the tenderId of the attendance is applying for

        Attendance attendance = new Attendance();
        attendance.setTenderId(tenderId);
        attendance.setWorkerId(workerId);
        attendance.setWorkerName(worker.getName());
        attendance.setMonthYear(monthYear);
        attendance.setSummary(new Attendance.AttendanceSummary());

        return attendance;
    }

    public Attendance getAttendance(String tenderId, String workerId, LocalDate date, Attendance.AttendanceStatus status) {
        YearMonth monthYear = YearMonth.from(date);
        
        Optional<Attendance> attendanceOpt = attendanceRepository
                .findByTenderIdAndWorkerIdAndMonthYear(tenderId, workerId, monthYear);
        
        if (attendanceOpt.isPresent()) {
            Attendance attendance = attendanceOpt.get();
            // Check if the specific date has the requested status
            Attendance.AttendanceStatus currentStatus = attendance.getDailyRecords().get(date);
            if (currentStatus == status) {
                return attendance;
            }
        }
        
        throw new RuntimeException("Attendance record not found for the specified criteria");
    }

    public void recordBulkAttendance(BulkAttendanceRequest request) {
        request.getWorkerStatuses().forEach((workerId, status) ->
                recordAttendance(
                        request.getTenderId(),
                        workerId,
                        request.getDate(),
                        status
                )
        );
    }

    public WorkerAttendanceHistory getWorkerHistory(String workerId, int months) {
        LocalDate startDate = LocalDate.now().minusMonths(months);
        List<Attendance> records = attendanceRepository
                .findByWorkerIdAndDateAfter(workerId, startDate);

        return WorkerAttendanceHistory.builder()
                .workerId(workerId)
                .attendanceByMonth(records.stream()
                        .collect(Collectors.groupingBy(
                                Attendance::getMonthYear,
                                Collectors.summarizingInt(a ->
                                        (int) a.getDailyRecords().values().stream()
                                                .filter(s -> s == Attendance.AttendanceStatus.PRESENT)
                                                .count()
                                )
                        )))
                .build();
    }
}