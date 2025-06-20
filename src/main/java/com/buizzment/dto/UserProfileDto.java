package com.buizzment.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class UserProfileDto {
    private String id;
    private String username;
    private String email;
    private List<OrgMembershipDto> orgMemberships;
}


