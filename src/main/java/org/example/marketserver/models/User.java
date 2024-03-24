package org.example.marketserver.models;

import jakarta.persistence.*;
import org.springframework.security.crypto.bcrypt.BCrypt;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    // It's important not to store passwords in plain text, so this field should contain a hash.
    @Column(nullable = false)
    private String passwordHash;

    @Column(nullable = false)
    private String name;

    // The contact number is nullable in case it's not provided.
    @Column
    private String contactNumber;

    // Field for the user's role. This could also be implemented as a Set of roles
    // if many-to-many relationships are used.
    @Column(nullable = false)
    private String role;

    // Field to represent whether the user account is active or not.
    @Column(nullable = false)
    private Boolean isActive;

    // Standard getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    // Use this setter to set the hashed password. Never store plain text passwords.
    public void setPassword(String password) {
        this.passwordHash = BCrypt.hashpw(password, BCrypt.gensalt());
    }

    // Method to check if an unhashed password matches the hashed password stored in the database
    public boolean checkPassword(String password) {
        return BCrypt.checkpw(password, this.passwordHash);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

}
