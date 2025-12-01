package com.buizzment.Service;

import com.buizzment.dto.BulkAttendanceRequestDto;
import com.buizzment.dto.BulkAttendanceResponseDto;
import com.buizzment.model.AttendanceV2;
import com.buizzment.model.AttendanceSheet;
import com.buizzment.repository.AttendanceRepository;
import com.buizzment.repository.AttendanceSheetRepository;
import com.buizzment.repository.AttendanceV2Repository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceV2Service {

    private final AttendanceV2Repository attendanceRepository;
    private final AttendanceSheetRepository attendanceSheetRepository;

    @Transactional
    public AttendanceSheet getOrCreateAttendanceSheet(String tenderId, YearMonth monthYear) {
        return attendanceSheetRepository.findByTenderIdAndMonthYear(tenderId, monthYear)
                .orElseGet(() -> attendanceSheetRepository.save(AttendanceSheet.builder()
                        .tenderId(tenderId)
                        .monthYear(monthYear)
                        .build()));
    }

    public AttendanceSheet createAttendanceSheet(AttendanceSheet sheet) {
        Optional<AttendanceSheet> attendanceSheetList = attendanceSheetRepository.findByTenderIdAndMonthYear(sheet.getTenderId(), sheet.getMonthYear());
         if(!attendanceSheetList.isEmpty()){
             throw new RuntimeException("Attendance Sheet already Exists for Tender");
         }

         return attendanceSheetRepository.save(AttendanceSheet.builder()
                 .tenderId(sheet.getTenderId())
                 .startDate(sheet.getStartDate())
                 .endDate(sheet.getEndDate())
                 .monthYear(sheet.getMonthYear())
                 .attendanceIds(sheet.getAttendanceIds() != null
                         ? sheet.getAttendanceIds()
                         : new ArrayList<>())
                 .build());

    }

    public List<AttendanceV2> getMonthlyAttendances(String tenderId, YearMonth monthYear) {
        AttendanceSheet sheet = getOrCreateAttendanceSheet(tenderId, monthYear);
        return attendanceRepository.findByIdIn(sheet.getAttendanceIds());
    }


//    @Transactional
//    public AttendanceV2 recordAttendance(String tenderId, String workerId,
//                                       LocalDate date, AttendanceV2.AttendanceStatus status) {
//
//        YearMonth monthYear = YearMonth.from(date);
//        AttendanceSheet sheet = getOrCreateAttendanceSheet(tenderId, monthYear);
//
//        // Find or create attendance record
//        AttendanceV2 attendance = attendanceRepository.findByWorkerId(workerId)
//                .orElseGet(() -> {
//                    AttendanceV2 newAttendance = new AttendanceV2();
//                    newAttendance.setWorkerId(workerId);
//                    return attendanceRepository.save(newAttendance);
//                });
//
//        // Update daily record
//        attendance.getDailyRecords().put(date, status);
//        AttendanceV2 savedAttendance = attendanceRepository.save(attendance);
//
//        // Add to sheet if not already present
//        if (!sheet.getAttendanceIds().contains(savedAttendance.getId())) {
//            sheet.getAttendanceIds().add(savedAttendance.getId());
//            attendanceSheetRepository.save(sheet);
//        }
//
//        return savedAttendance;
//    }

    public List<AttendanceSheet> getAttendanceSheetByTenderId(String tenderId){
        return attendanceSheetRepository.findByTenderId(tenderId);
    }

    @Transactional
    public BulkAttendanceResponseDto recordBulkAttendance(BulkAttendanceRequestDto request) {
        List<String> errors = new ArrayList<>();
        int successful = 0;
        int failed = 0;
        int totalRecords = 0;
        List<String> responseAttendanceIds= new ArrayList<>();
        Map<String, AttendanceV2> mapWorkerIdToAtt = getMapWorkerIdToAtt(request);


        // Process by worker - more efficient
        for (Map.Entry<String, Map<LocalDate, AttendanceV2.AttendanceStatus>> workerEntry :
                request.getWorkerAttendance().entrySet()) {

            String workerId = workerEntry.getKey();
            Map<LocalDate, AttendanceV2.AttendanceStatus> dateStatusMap = workerEntry.getValue();

            try {
                // Process all dates for this worker at once
                AttendanceV2 attendance = processWorkerAttendance(
                        request.getTenderId(),
                        workerId,
                        dateStatusMap,
                        mapWorkerIdToAtt
                );
                responseAttendanceIds.add(attendance.getId());
                successful++;
                totalRecords += dateStatusMap.size();

            } catch (Exception e) {
                failed++;
                errors.add("Worker " + workerId + ": " + e.getMessage());
            }
        }

        return new BulkAttendanceResponseDto(
                totalRecords,
                successful, // workers processed successfully
                failed,     // workers failed
                errors,
                responseAttendanceIds
        );
    }

    private Map<String, AttendanceV2> getMapWorkerIdToAtt(BulkAttendanceRequestDto request) {

        List<String> attendanceIds = attendanceSheetRepository.findById(request.getAttendanceSheetId())
                .map(AttendanceSheet::getAttendanceIds)
                .orElse(new ArrayList<>());


//        List<AttendanceV2> attendanceV2List = attendanceRepository.findByIdIn(attendanceIds);
        return attendanceRepository.findByIdIn(attendanceIds).stream()
                .collect(Collectors.toMap(
                        AttendanceV2::getWorkerId,
                        a -> a
                ));
    }

    @Transactional
    public AttendanceV2 processWorkerAttendance(String tenderId, String workerId,
                                              Map<LocalDate, AttendanceV2.AttendanceStatus> dateStatusMap,Map<String, AttendanceV2> mapWorkerIdToAtt) {
//        List<String> attendanceIdList = (attendanceSheetRepository.findById(sheetId).isPresent())?attendanceSheetRepository.findById(sheetId).get().getAttendanceIds():null;

        // Validate inputs
        if (tenderId == null || tenderId.trim().isEmpty()) {
            throw new IllegalArgumentException("Tender ID cannot be empty");
        }
        if (workerId == null || workerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Worker ID cannot be empty");
        }
        if (dateStatusMap == null || dateStatusMap.isEmpty()) {
            throw new IllegalArgumentException("Date status map cannot be empty");
        }

        // Get all unique months from the dates
        Set<YearMonth> months = dateStatusMap.keySet().stream()
                .map(YearMonth::from)
                .collect(Collectors.toSet());

        // Find or create attendance record for this worker
//        AttendanceV2 attendance = attendanceRepository.findByWorkerId(workerId)
//                .orElseGet(() -> {
//                    AttendanceV2 newAttendance = new AttendanceV2();
//                    newAttendance.setWorkerId(workerId);
//                    return attendanceRepository.save(newAttendance);
//                });

        AttendanceV2 attendance = mapWorkerIdToAtt.containsKey(workerId)?mapWorkerIdToAtt.get(workerId):
                    createAttendance(tenderId,workerId);

        // Update all dates for this worker
        attendance.getDailyRecords().putAll(dateStatusMap);
        AttendanceV2 savedAttendance = attendanceRepository.save(attendance);

        // Update attendance sheets for all months
        for (YearMonth month : months) {
            AttendanceSheet sheet = getOrCreateAttendanceSheet(tenderId, month);

            if (!sheet.getAttendanceIds().contains(savedAttendance.getId())) {
                sheet.getAttendanceIds().add(savedAttendance.getId());
                attendanceSheetRepository.save(sheet);
            }
        }

        return savedAttendance;
    }

    // Single date method now uses the bulk method internally
//    @Transactional
//    public AttendanceV2 recordAttendance(String tenderId, String workerId,
//                                       LocalDate date, AttendanceV2.AttendanceStatus status) {
//
//        Map<LocalDate, AttendanceV2.AttendanceStatus> singleDateMap = Map.of(date, status);
//        return processWorkerAttendance(tenderId, workerId, singleDateMap);
//    }

    public AttendanceV2 createAttendance(String tenderId, String workerId) {
        AttendanceV2 newAttendance = new AttendanceV2();
        if(Objects.isNull(workerId)){
            newAttendance.setWorkerId("test");
            return attendanceRepository.save(newAttendance);
        }else{
            newAttendance.setWorkerId(workerId);
            return attendanceRepository.save(newAttendance);
        }
    }

//    public void saveAttendanceSheetDetails(List<AttendanceV2> attendanceDetailsList){
//        for(AttendanceV2 att : attendanceDetailsList){
//            createAttendance(att.)
//        }
//    }

    public void deleteAttendance(String attendanceId,String sheetId){
//        Optional<AttendanceSheet> sheet = attendanceSheetRepository.findById(sheetId);
//        sheet.ifPresent(attendanceSheet -> attendanceSheet.getAttendanceIds().remove(attendanceId));
//        attendanceSheetRepository.save(sheet);
        Optional<AttendanceSheet> sheetOpt = attendanceSheetRepository.findById(sheetId);

        if (sheetOpt.isPresent()) {
            AttendanceSheet sheet = sheetOpt.get();

            // Prevent NPE
            if (sheet.getAttendanceIds() != null) {
                sheet.getAttendanceIds().remove(attendanceId);
            }

            attendanceSheetRepository.save(sheet);
        }

    }

    public void changeWorker(String attId,String workerId){
        Optional<AttendanceV2> sheetOpt = attendanceRepository.findById(attId);

        if (sheetOpt.isPresent()) {
            AttendanceV2 sheet = sheetOpt.get();

            // Prevent NPE
            if (sheet.getWorkerId() != null) {
                sheet.setWorkerId(workerId);
            }

            attendanceRepository.save(sheet);
        }
    }
}