package com.buizzment.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class OrgMembershipDto {
    private String orgId;
    private String orgName;
    private Set<String> roles;
    private Set<String> permissions;
}
