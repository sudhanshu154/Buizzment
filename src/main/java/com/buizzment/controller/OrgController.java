package com.buizzment.controller;

import com.buizzment.Service.AuthService;
import com.buizzment.Service.OrgService;
import com.buizzment.dto.OrgDetailResponse;
import com.buizzment.dto.OrgRequest;
import com.buizzment.dto.OrgResponse;
import com.buizzment.model.Org;
import com.buizzment.model.User;
import com.buizzment.security.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/orgs")
@RequiredArgsConstructor
public class OrgController {

    private final OrgService orgService;
    private final AuthService authService;

//    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<OrgResponse> createOrg(@RequestBody OrgRequest request, String creatorId) {
//        // Implementation
//        return ResponseEntity.ok().body(orgService.createOrganization(request,creatorId));
//    }
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrgResponse> createOrg(
            @Valid @RequestBody OrgRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        OrgResponse response = orgService.createOrganization(request, userPrincipal.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{orgId}")
    @PreAuthorize("@orgSecurity.isMember(#orgId)")
    public ResponseEntity<OrgDetailResponse> getOrganizationDetails(
            @PathVariable String orgId,
            Principal principal) {
        return ResponseEntity.ok(orgService.getOrganizationDetails(orgId,principal).getBody());
    }

//    @GetMapping("/{orgId}/members")
//    @PreAuthorize("hasRole('ORG_ADMIN') or @orgSecurity.isMember(#orgId)")
//    public ResponseEntity<List<User>> getOrgMembers(@PathVariable String orgId) {
//        // Implementation
//    }

//    @GetMapping("/{orgId}/projects")
//    @PreAuthorize("@orgSecurity.hasOrgPermission(#orgId, 'project.read')")
//    public ResponseEntity<List<Project>> getOrgProjects(@PathVariable String orgId) {
//        // Implementation
//    }

}
