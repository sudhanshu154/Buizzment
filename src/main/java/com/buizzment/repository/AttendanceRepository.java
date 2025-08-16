package com.buizzment.repository;

import com.buizzment.model.Attendance;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends MongoRepository<Attendance, String> {
    Optional<Attendance> findByTenderIdAndWorkerIdAndMonthYear(String tenderId, String workerId, YearMonth monthYear);
    List<Attendance> findByTenderIdAndMonthYear(String tenderId, YearMonth monthYear);
    List<Attendance> findByWorkerIdAndMonthYear(String workerId, YearMonth monthYear);

    @Query("{ 'workerId': ?0, 'dailyRecords': { $exists: true, $ne: {} }, $expr: { $gt: [ { $max: '$dailyRecords' }, ?1 ] } }")
    List<Attendance> findByWorkerIdAndDateAfter(String workerId, LocalDate date);
}