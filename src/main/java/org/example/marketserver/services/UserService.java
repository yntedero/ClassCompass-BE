package org.example.marketserver.services;

import lombok.RequiredArgsConstructor;
import org.example.marketserver.dtos.UserDTO;
import org.example.marketserver.models.User;
import org.example.marketserver.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getFirstName() + " " + userDTO.getLastName());

        user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));
        user.setContactNumber(userDTO.getContact());
        user.setRole(userDTO.getRole().toUpperCase());
        user.setIsActive(true);
        User savedUser = userRepository.save(user);
        return mapToDTO(savedUser);
    }

    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(this::mapToDTO);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Transactional
    public Optional<UserDTO> updateUser(Long id, UserDTO userDTO) {
        return userRepository.findById(id).map(user -> {
            user.setEmail(userDTO.getEmail());
            user.setName(userDTO.getFirstName() + " " + userDTO.getLastName());
            if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                user.setPasswordHash(passwordEncoder.encode(userDTO.getPassword()));
            }
            user.setContactNumber(userDTO.getContact());
            user.setRole(userDTO.getRole().toUpperCase());
            User updatedUser = userRepository.save(user);
            return mapToDTO(updatedUser);
        });
    }

    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getName().split(" ")[0]);
        dto.setLastName(user.getName().split(" ").length > 1 ? user.getName().split(" ")[1] : "");
        dto.setContact(user.getContactNumber());
        dto.setRole(user.getRole());
        dto.setStatus(user.getIsActive() ? "ACTIVE" : "INACTIVE");
        return dto;
    }


    public Optional<User> getUserEntityById(Long id) {
        return userRepository.findById(id);
    }
}
