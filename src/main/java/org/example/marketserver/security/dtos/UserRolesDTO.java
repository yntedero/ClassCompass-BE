package org.example.marketserver.security.dtos;

import java.util.Set;

public class UserRolesDTO {

    private final Long userId;
    private final Set<String> roles;

    public UserRolesDTO(Long userId, Set<String> roles) {
        this.userId = userId;
        this.roles = roles;
    }

    public Long getUserName() {
        return userId;
    }

    public Set<String> getRoles() {
        return roles;
    }

}
