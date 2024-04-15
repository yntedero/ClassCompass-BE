package org.example.marketserver.security.core;
import org.example.marketserver.security.core.MarketAuthenticationFilter;
import org.example.marketserver.security.core.MarketAuthenticationEntryPoint;
import org.example.marketserver.security.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private MarketAuthenticationEntryPoint authEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/roles").permitAll()
                        .requestMatchers("/api/authentication").permitAll()
                        .requestMatchers("/api/users").permitAll()
                        .anyRequest().permitAll())
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(customFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public OncePerRequestFilter customFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
                    throws ServletException, IOException {
                String requestURI = request.getRequestURI();
                if (requestURI.startsWith("/api/roles") ||
                        requestURI.startsWith("/api/authentication") ||
                        requestURI.startsWith("/api/users")) {
                    filterChain.doFilter(request, response);
                } else {
                    new MarketAuthenticationFilter(authenticationService).doFilter(request, response, filterChain);
                }
            }
        };
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers("/api/authentication");
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}