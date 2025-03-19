package com.alten.producttrialmasterbackend.controller;

import com.alten.producttrialmasterbackend.configuration.JwtUtils;
import com.alten.producttrialmasterbackend.dto.JwtResponse;
import com.alten.producttrialmasterbackend.dto.Login;
import com.alten.producttrialmasterbackend.entities.User;
import com.alten.producttrialmasterbackend.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    /**
     * endpoint qui permet de s'authentifier et générer le token
     *
     * @param login
     * @return
     */
    @PostMapping("/token")
    public ResponseEntity<Object> login(@RequestBody Login login) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(login.getEmail(), login.getPassword()));
        if (authentication.isAuthenticated()) {
            String token = jwtUtils.generateToken(login.getEmail());
            return ResponseEntity.ok(new JwtResponse(token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }

    /**
     * endpoint qui permet de créer un utilisateur
     *
     * @param user
     * @return
     */
    @PostMapping("/account")
    public ResponseEntity<Object> register(@RequestBody User user) {

        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return ResponseEntity.ok(userService.createUser(user));
    }

}
