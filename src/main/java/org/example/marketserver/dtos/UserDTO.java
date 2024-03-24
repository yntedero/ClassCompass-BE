package com.mustmarket.dto;

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

    // Additional fields for authentication data, roles, or status could be added here.
}
