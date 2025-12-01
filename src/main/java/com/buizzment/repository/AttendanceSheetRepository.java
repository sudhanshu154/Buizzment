package com.buizzment.repository;

import com.buizzment.model.AttendanceSheet;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceSheetRepository extends MongoRepository<AttendanceSheet, String> {
    Optional<AttendanceSheet> findByTenderIdAndMonthYear(String tenderId, YearMonth monthYear);
    boolean existsByTenderIdAndMonthYear(String tenderId, YearMonth monthYear);
    List<AttendanceSheet> findByTenderId(String tenderId);
    Optional<AttendanceSheet> findById(String attendanceSheetId);
}