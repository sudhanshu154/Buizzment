package com.buizzment.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Document(collection = "projects")
@CompoundIndexes({
        @CompoundIndex(name = "org_status_idx", def = "{'organization.$id': 1, 'status': 1}"),
        @CompoundIndex(name = "org_dates_idx", def = "{'organization.$id': 1, 'startingDate': 1, 'tentativeEndingDate': 1}")
})
@Data
@Builder
public class Project {
    @Id
    private String id;

    @NotBlank
    private String name;

    @NotBlank
    @Field("order_no")
    private String orderNo;

    @NotBlank
    @Field("el1_no")
    private String el1No;

    @Field("project_code")
    private String projectCode;

    @NotNull
    @Field("start_date")
    private LocalDate startingDate;

    @Field("tentative_end_date")
    private LocalDate tentativeEndingDate;

    @Field("actual_end_date")
    private LocalDate actualEndDate;

    @Field("status")
    private ProjectStatus status = ProjectStatus.PLANNING;

    @DBRef
    @Field("org_id")
    private Org organization;

    @Field("created_by")
    private String createdBy; // User ID

    @Field("created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Field("team_members")
    private Set<String> teamMemberIds = new HashSet<>();

    public enum ProjectStatus {
        PLANNING, IN_PROGRESS, ON_HOLD, COMPLETED, CANCELLED
    }

    public static final String SEQUENCE_NAME = "project_sequence";
}