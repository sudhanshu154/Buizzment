package com.buizzment.security;

import com.buizzment.model.User;
import com.buizzment.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("orgSecurity")
@AllArgsConstructor
public class OrgSecurity {

    private final UserRepository userRepository;

//    public boolean isMember(String orgId) {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();
//        return userRepository.findById(principal.getId())
//                .map(user -> user.getOrgRoles().containsKey(orgId))
//                .orElse(false);
//    }

    public boolean isMember(String orgId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();

        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return user.getOrgRoles().containsKey(orgId);
    }

    public boolean hasOrgPermission(String orgId, String permission) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) auth.getPrincipal();

        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return user.getOrgPermissions().getOrDefault(orgId, Set.of())
                .contains(permission) ||
                user.getOrgPermissions().getOrDefault(orgId, Set.of())
                        .contains("*"); // Wildcard permission
    }
}