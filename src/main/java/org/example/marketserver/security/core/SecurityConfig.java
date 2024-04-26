package org.example.marketserver.security.core;

import lombok.RequiredArgsConstructor;
import org.example.marketserver.config.CorsConfig;
import org.example.marketserver.security.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.example.marketserver.config.CorsConfig.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.CorsFilter;
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

//    private final UserDetailsServiceImpl userDetailsService;
//    private final JwtTokenUtil jwtTokenUtil;
    private final MarketAuthenticationFilter marketAuthenticationFilter;
    @Autowired
    private CorsFilter corsFilter;
    @Autowired
    private AuthenticationService authenticationService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
//                .cors(AbstractHttpConfigurer::disable)
//                .cors(cors -> cors.configurationSource(request -> new CorsConfiguration().applyPermitDefaultValues())) // Use non-deprecated method
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/authentication", "/index.html", "/ws/**").permitAll()
                        .anyRequest().authenticated()
                )
//                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(marketAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
//                .addFilterBefore(new MarketAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class
                .addFilterAfter(new MarketAuthenticationFilter(authenticationService), CorsFilter.class)
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
