package com.zchess.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // disable CSRF for REST API
            .csrf(csrf -> csrf.disable())

            // authorization rules
            .authorizeHttpRequests(auth -> auth

                // allow user APIs (register + login)
                .requestMatchers("/api/users/**").permitAll()

                // allow frontend files
                .requestMatchers(
                        "/",
                        "/chess.html",
                        "/chess.js",
                        "/chess.css",
                        "/pieces/**"
                ).permitAll()

                // allow health check
                .requestMatchers("/api/health").permitAll()

                // all other APIs require authentication
                .anyRequest().authenticated()
            )

            // enable basic auth (Postman testing)
            .httpBasic();

        return http.build();
    }

    // password encoder (required for MySQL user table)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}