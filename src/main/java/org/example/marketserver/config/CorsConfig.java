package org.example.marketserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        //config.addAllowedOriginPattern("*");
        config.setAllowedOriginPatterns(Arrays.asList("http://localhost:4200"));
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);//nemozem pridat localost:5500,4200
        config.addExposedHeader("Authorization"); // Add this line
        source.registerCorsConfiguration("/**", config);
        System.out.println("applying configs");
        return new CorsFilter(source);
    }
}
