package org.example.marketserver.security.core;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MarketAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if ( !StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ") ) {
            throw new AuthenticationCredentialsNotFoundException("Authorization header is missing or invalid");
        }

        String token = authHeader.substring("Bearer ".length()).trim(); // vytiahnutie tokenu z poziadavky

        // vystup: token, userRoles, username (zatial len username)
        String[] userRoles = new String[]{"ROLE_USER"}; // vytvorenie rolí

        List<SimpleGrantedAuthority> roles = Arrays.stream(userRoles).toList().stream()
                .map(
                        role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList()); // vytvorenie zoznamu rolí

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(token, null, roles); // vytvorenie autentifikacie
        SecurityContextHolder.getContext().setAuthentication(auth); // nastavenie autentifikacie
        filterChain.doFilter(request, response); // preposlanie poziadavky
    }
}