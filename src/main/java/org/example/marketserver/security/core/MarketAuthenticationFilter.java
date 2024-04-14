package org.example.marketserver.security.core;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.marketserver.security.dtos.UserRolesDTO;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.example.marketserver.security.services.AuthenticationService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MarketAuthenticationFilter extends OncePerRequestFilter {
    private final AuthenticationService authenticationService;

    public MarketAuthenticationFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if ( !StringUtils.hasLength(authHeader) || ! authHeader.startsWith("Bearer ")) {
            throw new AuthenticationCredentialsNotFoundException("Authentication failed!");
        }

        String token = authHeader.substring("Bearer".length()).trim();
        UserRolesDTO userRoles = authenticationService.authenticate(token);

        List<SimpleGrantedAuthority> roles = userRoles.getRoles().stream().map(
                role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList());

        UsernamePasswordAuthenticationToken auth
                = new UsernamePasswordAuthenticationToken(userRoles.getUserName(), null, roles);
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }
}