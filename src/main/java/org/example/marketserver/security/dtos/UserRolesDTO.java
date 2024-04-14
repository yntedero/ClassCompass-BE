package org.example.marketserver.security.dtos;

import java.util.Set;

public class UserRolesDTO {

    private final String userName;
    private final Set<String> roles;

    public UserRolesDTO(String userName, Set<String> roles) {
        this.userName = userName;
        this.roles = roles;
    }

    public String getUserName() {
        return userName;
    }

    public Set<String> getRoles() {
        return roles;
    }

}
