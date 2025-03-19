package com.alten.producttrialmasterbackend.service;

import com.alten.producttrialmasterbackend.entities.User;
import com.alten.producttrialmasterbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.security.core.userdetails.User.UserBuilder;
import static org.springframework.security.core.userdetails.User.builder;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    @Value("${default-admin-email}")
    private String defaultAdminEmail;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for email : %s".formatted(email)));

        UserBuilder userBuilder = builder()
                .username(user.getEmail())
                .password(user.getPassword());

        if (defaultAdminEmail.equals(user.getEmail())) {
            userBuilder.authorities(List.of(new SimpleGrantedAuthority("ADMIN")));
        }

        return userBuilder.build();
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
