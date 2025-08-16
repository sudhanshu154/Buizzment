package com.buizzment.controller;

import com.buizzment.Service.ExcelWorkerImportService;
import com.buizzment.dto.BulkImportResponse;
import com.buizzment.dto.BulkWorkerImportDto;
import com.buizzment.dto.WorkerCreateDto;
import com.buizzment.dto.WorkerResponseDto;
import com.buizzment.Service.WorkerService;
import com.buizzment.model.Worker;
import com.buizzment.security.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/workers")
@RequiredArgsConstructor
public class WorkerController {
    private final WorkerService workerService;

    @PostMapping
    public ResponseEntity<WorkerResponseDto> createWorker(
            @Valid @RequestBody WorkerCreateDto workerDto) {
        System.out.println("Create Worker Request: "+workerDto);
        WorkerResponseDto response = workerService.createWorker(workerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<Worker>> getOrgWorker(
            @RequestParam String orgId) {
        System.out.println("Searching for orgId: " + orgId);
        List<Worker> response = workerService.getOrgWorker(orgId);
        System.out.println("Found workers: " + response.size());
        return ResponseEntity.ok(response);
    }

    // Alternative: POST endpoint if you prefer request body
    @PostMapping("/by-org")
    public ResponseEntity<List<Worker>> getOrgWorkerByPost(
            @RequestBody String orgId) {
        System.out.println("Searching for orgId: " + orgId);
        List<Worker> response = workerService.getOrgWorker(orgId);
        System.out.println("Found workers: " + response.size());
        return ResponseEntity.ok(response);
    }

    private final ExcelWorkerImportService importService;

    @PostMapping("/bulk-import/{orgId}")
    public ResponseEntity<BulkImportResponse> bulkImportWorkers(
            @RequestParam("file") MultipartFile file,
            @PathVariable String orgId) {

        try {
            List<BulkWorkerImportDto> workers = importService.parseExcel(file);
            List<Worker> savedWorkers = workerService.saveBulkWorkers(workers,orgId);

            return ResponseEntity.ok(new BulkImportResponse(
                    savedWorkers.size(),
                    "Successfully imported workers"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new BulkImportResponse(
                    0,
                    "Failed to import: " + e.getMessage()
            ));
        }
    }
}