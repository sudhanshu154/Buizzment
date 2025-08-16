package com.buizzment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BulkWorkerImportDto {
    private String uan;
    private String memberId;
    private String name;
    private String gender;
    private LocalDate dob;
    private LocalDate doj;
    private String familyMemberName;
    private String relation;
    private String maritalStatus;
    private String mobile;
    private String email;
    private String aadhaar;
    private String pan;
    private String bankDetails;
    private boolean nominationFiled;
    private boolean aadhaarVerified;
}