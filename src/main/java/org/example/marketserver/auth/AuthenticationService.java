package org.example.marketserver.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.example.marketserver.config.JwtService;
import org.example.marketserver.models.User;
import org.example.marketserver.repositories.UserRepository;
import org.example.marketserver.token.Token;
import org.example.marketserver.token.TokenRepository;
import org.example.marketserver.token.TokenType;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository userRepository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    User newUser = new User();
    newUser.setEmail(request.getEmail());
    newUser.setFirstname(request.getFirstname());
    newUser.setLastname(request.getLastname());
    newUser.setPasswordHash(passwordEncoder.encode(request.getPassword()));
    newUser.setRole(request.getRole().toString()); // Assuming request.getRole() returns a Role object and you need to convert it to a String
    newUser.setIsActive(true); // Active by default or based on a confirmation process
    userRepository.save(newUser);

    String jwtToken = jwtService.generateToken(newUser);
    String refreshToken = jwtService.generateRefreshToken(newUser);
    saveUserToken(newUser, refreshToken, TokenType.REFRESH);

    return new AuthenticationResponse(jwtToken, refreshToken);
  }

  private void revokeAllUserTokens(Long userId) {
    tokenRepository.findAllValidTokenByUser(userId.intValue())
            .forEach(token -> {
              token.setExpired(true);
              token.setRevoked(true);
              tokenRepository.save(token);
            });
  }

  public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String refreshToken = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (refreshToken != null && refreshToken.startsWith("Bearer ")) {
      refreshToken = refreshToken.substring(7);
      String username = jwtService.extractUsername(refreshToken);
      Optional<User> userOptional = userRepository.findByEmail(username);

      if (userOptional.isPresent() && jwtService.validateToken(refreshToken, userOptional.get().getEmail())) {
        User user = userOptional.get();
        String newToken = jwtService.generateToken(user);
        AuthenticationResponse authResponse = new AuthenticationResponse(newToken, refreshToken);
        response.setContentType("application/json");
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
}
