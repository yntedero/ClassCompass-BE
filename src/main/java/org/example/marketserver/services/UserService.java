package org.example.marketserver.services;

import org.example.marketserver.dtos.UserDTO;
import org.example.marketserver.models.ChangePasswordRequest;
import org.example.marketserver.models.User;
import org.example.marketserver.models.Role;
import org.example.marketserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setFirstname(userDTO.getFirstName());
        user.setLastname(userDTO.getLastName());
        user.setContactNumber(userDTO.getContact());
        user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(Role.valueOf(userDTO.getRole().toUpperCase())); // Ensure Role exists
        user.setIsActive(userDTO.getStatus().equalsIgnoreCase("ACTIVE"));
        userRepository.save(user);
        return mapToDTO(user);
    }

    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(this::mapToDTO);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public UserDTO updateUser(Long id, UserDTO userDTO) {
        return userRepository.findById(id).map(user -> {
            user.setEmail(userDTO.getEmail());
            user.setFirstname(userDTO.getFirstName());
            user.setLastname(userDTO.getLastName());
            user.setContactNumber(userDTO.getContact());
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));
            }
            user.setRole(Role.valueOf(userDTO.getRole().toUpperCase())); // Handle role updates
            user.setIsActive(userDTO.getStatus().equalsIgnoreCase("ACTIVE"));
            userRepository.save(user);
            return mapToDTO(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + id));
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();

        // Check if the current password is correct
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPasswordHash())) {
            throw new IllegalStateException("Wrong password");
        }
        // Check if the new passwords match
        if (!request.getNewPassword().equals(request.getConfirmationPassword())) {
            throw new IllegalStateException("Passwords are not the same");
        }
        // Update and save the new password
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstname());
        dto.setLastName(user.getLastname());
        dto.setContact(user.getContactNumber());
        dto.setRole(user.getRole().name());
        dto.setStatus(user.getIsActive() ? "ACTIVE" : "INACTIVE");
        return dto;
    }
}
