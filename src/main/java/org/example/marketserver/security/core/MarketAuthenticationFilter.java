package org.example.marketserver.security.core;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.GrantedAuthority;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.marketserver.security.services.AuthenticationService;
import org.example.marketserver.security.dtos.UserRolesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.UrlPathHelper;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.core.Ordered;


@Component
public class MarketAuthenticationFilter extends OncePerRequestFilter{

    private final AuthenticationService authenticationService;
    private final List<String> excludedUrls = Arrays.asList("/api/authentication", "/api/roles", "/index.html");
    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    @Autowired
    public MarketAuthenticationFilter(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = urlPathHelper.getPathWithinApplication(request);
        return path.equals("/") || excludedUrls.contains(path) || request.getMethod().equals("OPTIONS") || path.matches("^/ws(/.*)?$");
    }
//    @Autowired
//    private CorsFilter corsFilter;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        System.out.println("authHeader: " + authHeader);
        if ( !StringUtils.hasLength(authHeader) || !authHeader.startsWith("Bearer ")) {
            throw new AuthenticationCredentialsNotFoundException("Authentication failed for" + request.getRequestURI());
        }
        String token = authHeader.substring("Bearer".length()).trim();
        UserRolesDTO userRoles = authenticationService.authenticate(token);

        String userRole = userRoles.getRole();
        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(userRole));

        CustomAuthentication auth
                = new CustomAuthentication(userRoles.getUserId(), userRoles.getUserId(), null, authorities, userRoles.getEmail());
        System.out.println("this is auffff" + auth);
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

}