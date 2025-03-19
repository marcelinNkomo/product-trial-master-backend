package com.alten.producttrialmasterbackend.configuration;

import com.alten.producttrialmasterbackend.filter.JwtFilter;
import com.alten.producttrialmasterbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.hasAuthority;

/**
 * Class qui permet de définir les règles de sécurité sur l'application
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private static final String ADMIN = "ADMIN";
    private final UserService userService;
    private final JwtUtils jwtUtils;

    /**
     * ici seul les chemins /account et /token sont accessibles
     * seul l'utilisateur ayant le droit ADMIN peut ajouter, modifier ou supprimer un produit
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/account", "/token").permitAll()
                        .requestMatchers(HttpMethod.POST, "/products").access(hasAuthority(ADMIN))
                        .requestMatchers(HttpMethod.PATCH, "/products/**").access(hasAuthority(ADMIN))
                        .requestMatchers(HttpMethod.DELETE, "/products/**").access(hasAuthority(ADMIN))
                        .anyRequest().authenticated()
                )
                // on applique le filtre jwt avant le filtre UsernamePasswordAuthenticationFilter
                .addFilterBefore(new JwtFilter(userService, jwtUtils), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    AuthenticationManager authenticationManager(HttpSecurity http, PasswordEncoder passwordEncoder) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userService).passwordEncoder(passwordEncoder);
        return authenticationManagerBuilder.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
