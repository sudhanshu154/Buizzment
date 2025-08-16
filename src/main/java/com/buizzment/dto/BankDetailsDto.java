package com.buizzment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BankDetailsDto {
    private String accountNumber;
    private String ifscCode;
    private String bankName;
    private String branch;
}
