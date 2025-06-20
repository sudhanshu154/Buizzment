package com.buizzment.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Builder
public class OrgDetailResponse {
    private String id;
    private String name;
    private String description;
    private Date createdAt;
    private String createdBy;
    private Set<String> availableRoles;
    private Set<String> yourPermissions;
}
