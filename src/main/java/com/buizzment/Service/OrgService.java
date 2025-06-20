package com.buizzment.Service;

import com.buizzment.ExceptionHandler.ResourceNotFoundException;
import com.buizzment.dto.OrgDetailResponse;
import com.buizzment.dto.OrgRequest;
import com.buizzment.dto.OrgResponse;
import com.buizzment.model.Org;
import com.buizzment.model.User;
import com.buizzment.repository.OrgRepository;
import com.buizzment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.security.Principal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class OrgService {

    private final OrgRepository orgRepository;
    private final UserRepository userRepository;
    private final PermissionService permissionService;
    private final AuthService authService;

    @Transactional
    public OrgResponse createOrganization(OrgRequest request, String creatorId) {
        // 1. Create organization
        Org org = new Org();
        org.setName(request.getName());
        org.setDescription(request.getDescription());
        org.setCreatedBy(creatorId);
        org.setPermissionTemplates(request.getInitialTemplates());

        Org savedOrg = orgRepository.save(org);

        // 2. Update creator's permissions
        User creator = userRepository.findById(creatorId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Add admin role
        if (creator.getOrgRoles() == null) {
            creator.setOrgRoles(new HashMap<>());
        }
        creator.getOrgRoles().computeIfAbsent(savedOrg.getId(), k -> new HashSet<>())
                .add("ORG_ADMIN");

        // Apply permissions
        permissionService.applyDefaultPermissions(creator, savedOrg.getId());
        userRepository.save(creator);

        // 3. Prepare response
        return OrgResponse.builder()
                .id(savedOrg.getId())
                .name(savedOrg.getName())
                .description(savedOrg.getDescription())
                .yourRoles(creator.getOrgRoles().get(savedOrg.getId()))
                .yourPermissions(creator.getOrgPermissions().get(savedOrg.getId()))
                .build();
    }

    public ResponseEntity<OrgDetailResponse> getOrganizationDetails(
            @PathVariable String orgId,
            Principal principal) {

        // 1. Get organization
        Org org = orgRepository.findById(orgId)
                .orElseThrow(() -> new ResourceNotFoundException("Organization not found"));

        // 2. Get current user's permissions
        String userId = authService.getUserIdFromPrincipal(principal);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 3. Build response
        OrgDetailResponse response = OrgDetailResponse.builder()
                .id(org.getId())
                .name(org.getName())
                .description(org.getDescription())
                .createdAt(org.getCreatedAt())
                .createdBy(org.getCreatedBy())
                .availableRoles(org.getPermissionTemplates().keySet())
                .yourPermissions(user.getOrgPermissions().getOrDefault(orgId, Set.of()))
                // Optional: Add member count
                // .memberCount(userRepository.countByOrgRolesContaining(orgId))
                .build();

        return ResponseEntity.ok(response);
    }

    public Org createOrg(OrgRequest request, String creatorId) {
        // 1. Create org document
        // 2. Assign creator as OrgAdmin
        // 3. Apply default permissions
        return null;
    }

    public void addUserToOrg(String orgId, String userId, Set<String> roles) {
        // 1. Verify inviter has ORG_ADMIN role
        // 2. Update user's orgRoles map
    }
}
