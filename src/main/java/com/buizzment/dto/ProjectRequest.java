package com.buizzment.dto;

import com.buizzment.model.Project;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class ProjectRequest {
    @NotBlank
    private String name;

    @NotBlank
    private String orderNo;

    @NotBlank
    private String el1No;

    private String projectCode;

    @PastOrPresent
    private LocalDate startingDate;

    @Future
    private LocalDate tentativeEndingDate;

    private Set<String> initialTeamMembers;
}

