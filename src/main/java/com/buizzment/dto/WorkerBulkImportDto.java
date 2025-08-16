package com.buizzment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class WorkerBulkImportDto {
    @NotBlank
    private String name;

    @NotBlank @Pattern(regexp = "\\d{12}")
    private String uanNumber;

    private String contactNumber;
    private Set<String> tags;
}