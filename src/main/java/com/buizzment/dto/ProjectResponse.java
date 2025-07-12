package com.buizzment.dto;

import com.buizzment.model.Project;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class ProjectResponse {
    private String id;
    private String name;
    private String orderNo;
    private String el1No;
    private String projectCode;
    private LocalDate startingDate;
    private LocalDate tentativeEndingDate;
    private Project.ProjectStatus status;
    private String orgId;
    private String orgName;
    private String createdBy;
    private LocalDateTime createdAt;
    private Set<String> teamMembers;
    private long taskCount;
}
