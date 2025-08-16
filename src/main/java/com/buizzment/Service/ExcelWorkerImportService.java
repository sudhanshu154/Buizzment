package com.buizzment.Service;

import com.buizzment.dto.BulkWorkerImportDto;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExcelWorkerImportService {

    public List<BulkWorkerImportDto> parseExcel(MultipartFile file) throws IOException {
        List<BulkWorkerImportDto> workers = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header

                BulkWorkerImportDto worker = new BulkWorkerImportDto();
                worker.setUan(formatUan(getUanValue(row.getCell(0))));  // Column A (UAN)
                worker.setName(getCellStringValue(row, 2));  // Column C (Name)
                worker.setGender(getCellStringValue(row, 3));
                worker.setDob(parseDate(getCellStringValue(row, 4)));
                worker.setDoj(parseDate(getCellStringValue(row,5)));
                worker.setFamilyMemberName(getCellStringValue(row,6));
                worker.setBankDetails(getCellStringValue(row,13));
                worker.setMobile(getCellStringValue(row,9));
                // Map all other fields...

                workers.add(worker);
            }
        }
        return workers;
    }

    private String formatUan(String rawUan) {
        // Remove all non-digit characters
        String digits = rawUan.replaceAll("[^0-9]", "");

        // Ensure exactly 12 digits (pad with leading zeros if shorter)
        return String.format("%12s", digits).replace(' ', '0').substring(0, 12);
    }
    private String getUanValue(Cell cell) {
        if (cell == null) return "";

        switch (cell.getCellType()) {
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    throw new IllegalArgumentException("UAN cannot be a date");
                }
                // Force full precision numeric conversion
                BigDecimal value = BigDecimal.valueOf(cell.getNumericCellValue());
                return value.toPlainString().split("\\.")[0]; // Remove decimal part

            case STRING:
                return cell.getStringCellValue().trim();

            case FORMULA:
                switch (cell.getCachedFormulaResultType()) {
                    case NUMERIC:
                        return BigDecimal.valueOf(cell.getNumericCellValue()).toPlainString().split("\\.")[0];
                    case STRING:
                        return cell.getStringCellValue().trim();
                    default:
                        throw new IllegalArgumentException("Unsupported formula result type for UAN");
                }

            default:
                throw new IllegalArgumentException("Unsupported cell type for UAN");
        }
    }
    private String getCellStringValue(Row row, int cellNum) {
        Cell cell = row.getCell(cellNum, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
        return cell.toString().trim();
    }

    private LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (Exception e) {
            return null;
        }
    }
}