package com.buizzment.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Data
@Document(collection = "users")
public class User implements UserDetails {
    @Id
    private String id;

    private String username;
    private String password;
    private String email;
    private boolean enabled = true;

//    private Set<String> roles = new HashSet<>();
    private Set<String> roles = Set.of("WORKER");  // Default role
    private Set<String> permissions = new HashSet<>();

    @DBRef
    private Org primaryOrg;

    @Field("org_roles")
    private Map<String, Set<String>> orgRoles; // orgId -> roles
    // Key: OrgID, Value: Set of roles

    @Field("org_permissions")
    private Map<String, Set<String>> orgPermissions = new HashMap<>();
    // Key: OrgID, Value: Set of permissions

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<GrantedAuthority> authorities = new HashSet<>();

        // Add roles as authorities with ROLE_ prefix
        authorities.addAll(roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet()));

        // Add permissions as authorities
        authorities.addAll(permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet()));

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}