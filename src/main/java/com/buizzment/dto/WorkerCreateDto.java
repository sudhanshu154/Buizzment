package com.buizzment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WorkerCreateDto {
    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 50, message = "Name must be 2-50 characters")
    private String name;

    @NotBlank(message = "UAN number is mandatory")
    @Pattern(regexp = "\\d{12}", message = "UAN must be 12 digits")
    private String uanNumber;

    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Invalid phone number")
    private String contactNumber;

    @Valid
    private BankDetailsDto bankDetails;

    private Set<String> tags;

    private String designation;

    @NotEmpty(message = "Org Id is mandatory")
    @JsonProperty("org_ids")
    private Set<String> orgIds;
}