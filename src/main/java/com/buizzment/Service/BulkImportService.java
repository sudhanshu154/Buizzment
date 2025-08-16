package com.buizzment.Service;

import com.buizzment.dto.WorkerResponseDto;
import com.buizzment.model.Worker;
import com.buizzment.repository.WorkerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import java.util.Arrays;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BulkImportService {
    private final WorkerRepository workerRepository;

    @Transactional
    public List<WorkerResponseDto> importWorkersFromCsv(MultipartFile file) {
        try (Reader reader = new InputStreamReader(file.getInputStream());
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim())) {

            return csvParser.getRecords().stream()
                    .map(this::parseWorker)
                    .map(workerRepository::save)
                    .map(this::mapToWorkerResponse)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse CSV", e);
        }
    }

    private Worker parseWorker(CSVRecord record) {
        Worker worker = new Worker();
        worker.setName(record.get("name"));
        worker.setUanNumber(record.get("uanNumber"));

        if (record.isMapped("contactNumber")) {
            worker.setContactNumber(record.get("contactNumber"));
        }

        if (record.isMapped("tags")) {
            worker.setTags(Arrays.stream(record.get("tags").split(";"))
                    .filter(tag -> !tag.isEmpty())
                    .collect(Collectors.toSet()));
        }

        return worker;
    }

    private WorkerResponseDto mapToWorkerResponse(Worker worker) {
        // Your mapping logic here
        return WorkerResponseDto.builder()
                .name(worker.getName())
                .uanNumber(worker.getUanNumber())
                .contactNumber(worker.getContactNumber())
                .tags(worker.getTags())
                .build();
    }
}