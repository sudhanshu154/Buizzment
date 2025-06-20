package com.buizzment.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
@Builder
public class OrgResponse {
    private String id;
    private String name;
    private String description;
    private Set<String> yourRoles;
    private Set<String> yourPermissions;

    // Add if you want to show member count
    private Integer memberCount;
}


