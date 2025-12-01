package com.buizzment.Service;

import com.buizzment.dto.BankDetailsDto;
import com.buizzment.dto.BulkWorkerImportDto;
import com.buizzment.dto.WorkerCreateDto;
import com.buizzment.dto.WorkerResponseDto;
import com.buizzment.model.Worker;
import com.buizzment.repository.WorkerRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkerService {
    private final WorkerRepository workerRepository;

    @Transactional
    public WorkerResponseDto createWorker(WorkerCreateDto dto) {
        Worker worker = new Worker();
        worker.setName(dto.getName());
        worker.setUanNumber(dto.getUanNumber());
        worker.setContactNumber(dto.getContactNumber());
        worker.setOrgIds(dto.getOrgIds());
        worker.setDesignation(dto.getDesignation());

        if (dto.getBankDetails() != null) {
            worker.setBankDetails(Worker.BankDetails.builder()
                    .accountNumber(dto.getBankDetails().getAccountNumber())
                    .ifscCode(dto.getBankDetails().getIfscCode())
                    .bankName(dto.getBankDetails().getBankName())
                    .branch(dto.getBankDetails().getBranch())
                    .build());
        }

        if (dto.getTags() != null) {
            worker.setTags(dto.getTags());
        }

        Worker savedWorker = workerRepository.save(worker);
        return mapToWorkerResponse(savedWorker);
    }

    private WorkerResponseDto mapToWorkerResponse(Worker worker) {
        WorkerResponseDto response = new WorkerResponseDto();
        response.setId(worker.getId());
        response.setName(worker.getName());
        response.setUanNumber(worker.getUanNumber());
        response.setContactNumber(worker.getContactNumber());
        response.setTenderIds(worker.getTenderIds());
        response.setTags(worker.getTags());
        response.setActive(worker.isActive());
        response.setOrgIds(worker.getOrgIds());
        response.setDesignation(worker.getDesignation());

        if (worker.getBankDetails() != null) {
            BankDetailsDto bankDetails = new BankDetailsDto();
            bankDetails.setAccountNumber(worker.getBankDetails().getAccountNumber());
            bankDetails.setIfscCode(worker.getBankDetails().getIfscCode());
            bankDetails.setBankName(worker.getBankDetails().getBankName());
            bankDetails.setBranch(worker.getBankDetails().getBranch());
            response.setBankDetails(bankDetails);
        }

        return response;
    }

    public List<Worker> getOrgWorker(@Valid String orgId) {
        // log.info("In get Org Worker");
        return workerRepository.findByOrgIdsContaining(orgId);
    }

    @Transactional
    public List<Worker> saveBulkWorkers(List<BulkWorkerImportDto> workerDtos, String orgId) {
        return workerDtos.stream()
                .map(dto -> {
                    Worker worker = new Worker();
                    worker.setUanNumber(dto.getUan());
                    worker.setName(dto.getName());
                    worker.setMobileNo(dto.getMobile());
                    worker.setDoj(dto.getDoj());
                    worker.setGender(dto.getGender());
                    worker.setDob(dto.getDob());
                    worker.setOrgIds(Set.of(orgId));

                    if (dto.getBankDetails() != null && !dto.getBankDetails().contains("NOT AVAILABLE")) {
                        // Split the string by comma
                        String[] parts = dto.getBankDetails().split(",");

                        Worker.BankDetails bankDetails = Worker.BankDetails.builder()
                                .accountNumber(parts[0])
                                .ifscCode(parts[1])
                                .build();
                    }

                    return workerRepository.save(worker);
                })
                .collect(Collectors.toList());
    }
}