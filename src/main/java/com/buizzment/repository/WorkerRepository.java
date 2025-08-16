package com.buizzment.repository;

import com.buizzment.model.Worker;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkerRepository extends MongoRepository<Worker, String> {
    Optional<Worker> findByUanNumber(String uanNumber);
    List<Worker> findByTenderIdsContaining(String tenderId);
    List<Worker> findByIsActive(boolean isActive);
//    List<Worker> findByOrgIdContaining(String orgId);
    List<Worker> findByOrgIdsContaining(String orgId); // Find workers by org ID
}