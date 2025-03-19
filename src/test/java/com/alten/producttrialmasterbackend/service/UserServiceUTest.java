package com.alten.producttrialmasterbackend.service;

import com.alten.producttrialmasterbackend.entities.User;
import com.alten.producttrialmasterbackend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class UserServiceUTest {
    @InjectMocks
    UserService userService;
    @Mock
    UserRepository userRepository;

    @Test
    void shouldCreateUser() {
        User user = new User();
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.createUser(user);

        verify(userRepository).save(user);
    }

    @Test
    void shouldFindUserByEmail() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        userService.findByEmail("fake_email@email.com");

        verify(userRepository).findByEmail("fake_email@email.com");
    }

    @Test
    void loadUserByUsernameShouldThrowExceptionWhenUserNotExist() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.loadUserByUsername("test@test.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessage("User not found for email : test@test.com");
    }

    @Test
    void loadUserByUsernameShouldAddAdminAuhtority() {
        setField(userService, "defaultAdminEmail", "admin@admin.com");
        User user = new User();
        user.setEmail("admin@admin.com");
        user.setPassword("admin_pwd");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("admin@admin.com");

        SimpleGrantedAuthority admin = new SimpleGrantedAuthority("ADMIN");
        List<SimpleGrantedAuthority> authorities = userDetails.getAuthorities().stream()
                .map(auth -> new SimpleGrantedAuthority(auth.getAuthority()))
                .toList();

        assertThat(authorities).containsOnly(admin);
    }

    @Test
    void loadUserByUsernameShouldNotAddAdminAuhtority() {
        setField(userService, "defaultAdminEmail", "admin@admin.com");
        User user = new User();
        user.setEmail("test@test.com");
        user.setPassword("basic_pwd");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        UserDetails userDetails = userService.loadUserByUsername("test@test.com");

        assertThat(userDetails.getAuthorities()).isEmpty();
    }
}