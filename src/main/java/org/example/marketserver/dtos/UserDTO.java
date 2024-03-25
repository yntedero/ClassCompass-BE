package org.example.marketserver.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String contact;

    private String passwordHash;

    private String role;

    // Account status "ACTIVE", "SUSPENDED", "DEACTIVATED"
    private String status;
}

