package com.buizzment.dto;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;
import java.util.Set;

// New Org DTOs
@Data
public class OrgRequest {
    private String name;
    private String description;
    private Set<String> defaultPermissions;
    @Field("initial_templates")
    private Map<String, Set<String>> initialTemplates = Map.of(
            "ORG_ADMIN", Set.of("org.*", "user.manage", "settings.*"),
            "ORG_MEMBER", Set.of("org.read", "project.create")
    );
}

