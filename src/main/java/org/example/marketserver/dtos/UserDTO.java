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
    private String contact; // Can be used for phone numbers or other forms of contact.

    // The password should be a hash. Never store or transmit plain-text passwords.
    // This field can be used when creating or updating the user's password.
    private String passwordHash;

    // Role field for authorization purposes. E.g., "ADMIN", "USER"
    private String role;

    // Account status, e.g., "ACTIVE", "SUSPENDED", "DEACTIVATED"
    private String status;
}
