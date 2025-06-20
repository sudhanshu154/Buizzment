package com.buizzment.Service;

import com.buizzment.model.Org;
import com.buizzment.model.User;
import com.buizzment.repository.OrgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final OrgRepository orgRepository;

    public Set<String> resolvePermissions(String orgId, Set<String> roles) {
        Org org = orgRepository.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        Set<String> permissions = new HashSet<>();

        // Get permissions from each role
        roles.forEach(role -> {
            if (org.getPermissionTemplates().containsKey(role)) {
                permissions.addAll(org.getPermissionTemplates().get(role));
            }
        });

        return permissions;
    }

    public void applyDefaultPermissions(User user, String orgId) {
        Org org = orgRepository.findById(orgId)
                .orElseThrow(() -> new RuntimeException("Organization not found"));

        // Add default roles
        user.getOrgRoles().computeIfAbsent(orgId, k -> new HashSet<>())
                .addAll(org.getDefaultRoles());

        // Resolve and store permissions
        Set<String> permissions = resolvePermissions(orgId, user.getOrgRoles().get(orgId));
        user.getOrgPermissions().put(orgId, permissions);
    }
}