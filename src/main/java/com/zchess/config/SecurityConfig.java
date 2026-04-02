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
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth

             
                .requestMatchers("/api/users/register").permitAll()
                .requestMatchers("/api/users/login").permitAll()

             
                .requestMatchers(
                    "/",
                    "/chess.html",
                    "/chess.js",
                    "/chess.css",
                    "/admin.html",
                    "/pieces/**"
                ).permitAll()

           
                .requestMatchers("/api/health").permitAll()

             
                .anyRequest().authenticated()
            )
          
            .httpBasic(httpBasic -> {});

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}