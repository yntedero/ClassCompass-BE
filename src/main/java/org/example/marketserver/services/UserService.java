package org.example.marketserver.services;

import org.example.marketserver.repositories.UserRepository;
import org.example.marketserver.dtos.UserDTO;
import org.example.marketserver.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

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

    public UserDTO createUser(UserDTO userDTO) {
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setName(userDTO.getFirstName() + " " + userDTO.getLastName());
        user.setPassword(userDTO.getPasswordHash());
        user.setContactNumber(userDTO.getContact());
        user.setRole(userDTO.getRole());
        user.setIsActive(true);
        user = userRepository.save(user);
        return mapToDTO(user);
    }

    public Optional<UserDTO> getUserById(Long id) {
        return userRepository.findById(id).map(this::mapToDTO);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public Optional<UserDTO> updateUser(Long id, UserDTO userDTO) {
        return userRepository.findById(id).map(user -> {
            user.setEmail(userDTO.getEmail());
            user.setName(userDTO.getFirstName() + " " + userDTO.getLastName());
            if (userDTO.getPasswordHash() != null && !userDTO.getPasswordHash().isEmpty()) {
                user.setPassword(userDTO.getPasswordHash());
            }
            user.setContactNumber(userDTO.getContact());
            userRepository.save(user);
            return mapToDTO(user);
        });
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO mapToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFirstName(user.getName().split(" ")[0]);
        userDTO.setLastName(user.getName().split(" ").length > 1 ? user.getName().split(" ")[1] : "");
        userDTO.setContact(user.getContactNumber());
        userDTO.setRole(user.getRole());
        userDTO.setStatus(user.getIsActive() ? "ACTIVE" : "INACTIVE");
        return userDTO;
    }
}