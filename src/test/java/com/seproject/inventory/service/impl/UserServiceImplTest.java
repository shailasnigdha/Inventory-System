package com.seproject.inventory.service.impl;

import com.seproject.inventory.dto.UserRegisterDto;
import com.seproject.inventory.entity.Role;
import com.seproject.inventory.entity.User;
import com.seproject.inventory.exception.BadRequestException;
import com.seproject.inventory.exception.ResourceNotFoundException;
import com.seproject.inventory.repository.RoleRepository;
import com.seproject.inventory.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegisterDto dto;

    @BeforeEach
    void setUp() {
        dto = new UserRegisterDto();
        dto.setUsername("alice");
        dto.setEmail("alice@example.com");
        dto.setPassword("password123");
    }

    @Test
    void registerUser_Success() {
        Role role = Role.builder().id(1L).name("BUYER").build();

        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(roleRepository.findByName("BUYER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("password123")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User saved = userService.registerUser(dto, "BUYER");

        assertEquals("alice", saved.getUsername());
        assertEquals("encoded", saved.getPassword());
        assertEquals(1, saved.getRoles().size());
    }

    @Test
    void registerUser_ThrowsWhenUsernameAlreadyExists() {
        when(userRepository.existsByUsername("alice")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userService.registerUser(dto, "BUYER"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_ThrowsWhenEmailAlreadyExists() {
        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(true);

        assertThrows(BadRequestException.class, () -> userService.registerUser(dto, "BUYER"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_ThrowsWhenRoleMissing() {
        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(roleRepository.findByName("BUYER")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.registerUser(dto, "BUYER"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_AssignsSingleRequestedRole() {
        Role role = Role.builder().id(2L).name("SELLER").build();

        when(userRepository.existsByUsername("alice")).thenReturn(false);
        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(roleRepository.findByName("SELLER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode("password123")).thenReturn("encoded");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User saved = userService.registerUser(dto, "SELLER");

        assertEquals(Set.of(role), saved.getRoles());
    }
}

