package org.example.marketserver.security.services;


import org.example.marketserver.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.example.marketserver.security.dtos.UserRolesDTO;
import org.example.marketserver.security.entities.RoleEntity;
import org.example.marketserver.security.entities.TokenEntity;
import org.example.marketserver.security.repositories.TokenRepository;
import org.example.marketserver.repositories.UserRepository;
import org.example.marketserver.models.User;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AuthenticationService {
    private static final int TOKEN_VALIDITY_IN_MINUTES = 15;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    // https://bcrypt-generator.com/, round 1


    public AuthenticationService(UserRepository userRepository, TokenRepository tokenRepository) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Transactional
    public String authenticate(String email, String password) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (optionalUser.isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Username and/or password do not match!");
        }

        if (!passwordEncoder.matches(password, optionalUser.get().getPasswordHash())) {
            throw new AuthenticationCredentialsNotFoundException("Username and/or password do not match!");
        }

        TokenEntity token = new TokenEntity();
        String randomString = UUID.randomUUID().toString();
        token.setToken(randomString);
        token.setUser(optionalUser.get());
        token.setCreatedAt(LocalDateTime.now());
        tokenRepository.save(token);
        return token.getToken();
    }

//    public UserDTO getUserDetails() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String userIdString = (String) authentication.getPrincipal();
//        System.out.println("userIdString: " + userIdString);
//        Long userId = Long.valueOf(userIdString);
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found with id " + userId));
//        return this.mapToDTO(user);
//    }
//    private UserDTO mapToDTO(User user) {
//        UserDTO dto = new UserDTO();
//        dto.setId(user.getId());
//        dto.setEmail(user.getEmail());
//        dto.setFirstName(user.getName().split(" ")[0]);
//        dto.setLastName(user.getName().split(" ").length > 1 ? user.getName().split(" ")[1] : "");
//        dto.setContact(user.getContactNumber());
//        dto.setRole(user.getRole());
//        dto.setStatus(user.getIsActive() ? "ACTIVE" : "INACTIVE");
//        return dto;
//    }
    @Transactional
    public UserRolesDTO authenticate(String token) {
        Optional<TokenEntity> optionalToken = tokenRepository.findByToken(token);

        if (optionalToken.isEmpty()) {
            throw new AuthenticationCredentialsNotFoundException("Authentication failed!");
        }

        validateTokenExpiration(optionalToken.get());

        String role = optionalToken.get().getUser().getRole();

        return new UserRolesDTO(optionalToken.get().getUser().getId(), optionalToken.get().getUser().getEmail(), role);
    }
    public void validateTokenExpiration(TokenEntity token) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tokenExpiration = token.getCreatedAt().plus(TOKEN_VALIDITY_IN_MINUTES, ChronoUnit.MINUTES);

        if (now.isAfter(tokenExpiration)) {
            throw new AuthenticationCredentialsNotFoundException("Authentication failed!");
        }
    }
    @Transactional
    public boolean validateToken(String token) {
        Optional<TokenEntity> optionalToken = tokenRepository.findByToken(token);

        if (optionalToken.isEmpty()) {
            // Token not found
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tokenExpiration = optionalToken.get().getCreatedAt().plus(TOKEN_VALIDITY_IN_MINUTES, ChronoUnit.MINUTES);

        return !now.isAfter(tokenExpiration);
    }

    @Transactional
    public void tokenRemove(String token) {
        tokenRepository.deleteByToken(token);
    }

}
