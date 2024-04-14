package org.example.marketserver.models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum Role {
    ADMIN(Set.of("ROLE_ADMIN", "ACCESS_READ", "ACCESS_WRITE")),
    USER(Set.of("ROLE_USER", "ACCESS_READ")),
    GUEST(Set.of("ROLE_GUEST"));

    private final Set<String> permissions;

    Role(Set<String> permissions) {
        this.permissions = permissions;
    }

    public Set<GrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());
    }
}
