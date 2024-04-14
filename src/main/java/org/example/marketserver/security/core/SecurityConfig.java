package org.example.marketserver.security.core;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) //ochrany mechanizmus, ktory sa pouziva na ochranu pred CSRF utokmi
                .authorizeHttpRequests(auth ->
                        auth.anyRequest().permitAll() // kazda poziadavka musi byt overena
                );
        // http.addFilterBefore(new MarketAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class); // pridanie filtra
        return http.build(); // vytvorenie filtra
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}