package com.buizzment.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.*;

@Data
@Document(collection = "orgs")
public class Org {
    @Id
    private String id;

    private String name;
    private String description;

    @Field("admin_users")
    private Set<String> adminUsers = new HashSet<>();

    @Field("created_at")
    private Date createdAt = new Date();

    @Field("created_by")
    private String createdBy; // User ID

    private Set<String> roles = new HashSet<>();
    private Set<String> permissions = new HashSet<>();
    @Field("default_roles")
    private Set<String> defaultRoles = Set.of("ORG_MEMBER");

    @Field("permission_templates")
    private Map<String, Set<String>> permissionTemplates = new HashMap<>();
}

