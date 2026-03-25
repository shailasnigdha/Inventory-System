package com.seproject.inventory.service.impl;

import com.seproject.inventory.dto.UserRegisterDto;
import com.seproject.inventory.entity.Role;
import com.seproject.inventory.entity.User;
import com.seproject.inventory.repository.RoleRepository;
import com.seproject.inventory.repository.UserRepository;
import com.seproject.inventory.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(UserRegisterDto dto, String roleName) {

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = User.builder()
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .roles(Set.of(role))
                .build();

        return userRepository.save(user);
    }
}
