package com.buizzment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkerResponseDto {
    private String id;
    private String name;
    private String uanNumber;
    private String contactNumber;
    private BankDetailsDto bankDetails;
    private Set<String> tenderIds;
    @JsonProperty("org_ids")
    private Set<String> orgIds;
    private Set<String> tags;
    private boolean isActive;
    private String designation;

    // Reuse the BankDetailsDto from WorkerCreateDto
}
