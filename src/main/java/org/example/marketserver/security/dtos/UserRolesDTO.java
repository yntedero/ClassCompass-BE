package org.example.marketserver.security.dtos;

import java.util.Set;

public class UserRolesDTO {

    private final Long id;
    private final String email;
    private final String role;

    public UserRolesDTO(Long id, String email,String role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public Long getUserId() {
        return id;
    }
    public String getUserName(){return email;}
    public String getRole() {
        return role;
    }

}
