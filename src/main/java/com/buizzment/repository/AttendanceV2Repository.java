package com.buizzment.repository;

import com.buizzment.model.AttendanceV2;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceV2Repository extends MongoRepository<AttendanceV2, String> {
    List<AttendanceV2> findByIdIn(List<String> attendanceIds);
    Optional<AttendanceV2> findByWorkerId(String workerId);
}