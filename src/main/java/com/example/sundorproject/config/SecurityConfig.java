package com.example.sundorproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Public Read-Only & Static routes
                        .requestMatchers(
                                "/",
                                "/books",
                                "/categories",
                                "/login",
                                "/register",
                                "/users/register",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/favicon.ico",
                                "/error"
                        ).permitAll()

                        // Restricted Admin-only routes
                        .requestMatchers(
                                "/books/new",
                                "/books/save",
                                "/books/delete/**",
                                "/categories/new",
                                "/categories/save",
                                "/categories/delete/**",
                                "/users/**",
                                "/borrows/issue",
                                "/borrows/return/**"
                        ).hasRole("ADMIN")

                        // Authenticated User & Admin routes
                        .requestMatchers(
                                "/borrows/my-history"
                        ).hasAnyRole("USER", "ADMIN")

                        // Borrows list (admin can see all)
                        .requestMatchers("/borrows").hasRole("ADMIN")

                        // Any other request requires authentication
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/books", true)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/books?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}

