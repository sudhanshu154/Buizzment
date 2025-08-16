package com.buizzment.controller;

import com.buizzment.Service.BulkImportService;
import com.buizzment.dto.WorkerResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/bulk")
@RequiredArgsConstructor
public class BulkImportController {
    private final BulkImportService bulkImportService;

    @PostMapping("/workers/import")
    public ResponseEntity<List<WorkerResponseDto>> importWorkers(
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(bulkImportService.importWorkersFromCsv(file));
    }
}