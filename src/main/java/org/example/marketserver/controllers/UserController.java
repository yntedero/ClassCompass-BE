package org.example.marketserver.controllers;

import org.example.marketserver.dtos.UserDTO;
import org.example.marketserver.models.User;
import org.example.marketserver.repositories.UserRepository;
import org.example.marketserver.security.services.AuthenticationService;
import org.example.marketserver.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, AuthenticationService authenticationService, UserRepository userRepositor) {
        this.userService = userService;
        this.userRepository = userRepositor;
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        UserDTO createdUser = userService.createUser(userDTO);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(id, userDTO)
                .orElseThrow(() -> new RuntimeException("User not found with id " + id));
        return ResponseEntity.ok(updatedUser);
    }
    @GetMapping("/user-details")
    public ResponseEntity<UserDTO> getUserDetails() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Long userId = (Long) authentication.getPrincipal();
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            UserDTO userDTO = convertUserToUserDTO(user.get());
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    private UserDTO convertUserToUserDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getName().split(" ")[0]);
        dto.setLastName(user.getName().split(" ").length > 1 ? user.getName().split(" ")[1] : "");
        dto.setContact(user.getContactNumber());
        dto.setRole(user.getRole());
        dto.setStatus(user.getIsActive() ? "ACTIVE" : "INACTIVE");
        System.out.println("id:" + dto.getId() + " email:" + dto.getEmail() + " firstName:" + dto.getFirstName() + " lastName:" + dto.getLastName() + " contact:" + dto.getContact() + " role:" + dto.getRole() + " status:" + dto.getStatus());

        return dto;
    }
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or #id == authentication.principal")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}