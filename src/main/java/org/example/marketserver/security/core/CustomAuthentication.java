package org.example.marketserver.security.core;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class CustomAuthentication extends UsernamePasswordAuthenticationToken {
    private final Long id;
    private final String email;
    public CustomAuthentication(Long id, Long principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String email) {
        super(principal, credentials, authorities);
        this.id = id;
        this.email = email;
    }
    public Long getId() {
        return id;
    }
    public String getEmail() {
        return email;
    }
}