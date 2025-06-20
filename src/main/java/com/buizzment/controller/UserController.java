package com.buizzment.controller;

import com.buizzment.Service.AuthService;
import com.buizzment.dto.OrgMembershipDto;
import com.buizzment.dto.UserProfileDto;
import com.buizzment.model.Org;
import com.buizzment.model.User;
import com.buizzment.repository.OrgRepository;
import com.buizzment.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    private final OrgRepository orgRepository;
    private final AuthService authService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileDto> getCurrentUserProfile(Principal principal) {
        String userId = authService.getUserIdFromPrincipal(principal);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<OrgMembershipDto> orgMemberships = user.getOrgRoles().keySet().stream()
                .map(orgId -> {
                    Org org = orgRepository.findById(orgId)
                            .orElse(new Org()); // Handle case where org might be deleted
                    return OrgMembershipDto.builder()
                            .orgId(orgId)
                            .orgName(org.getName())
                            .roles(user.getOrgRoles().get(orgId))
                            .permissions(user.getOrgPermissions().getOrDefault(orgId, Set.of()))
                            .build();
                })
                .collect(Collectors.toList());

        UserProfileDto response = UserProfileDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .orgMemberships(orgMemberships)
                .build();

        return ResponseEntity.ok(response);
    }
}