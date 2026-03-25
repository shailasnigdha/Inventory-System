package com.seproject.inventory.controller;

import com.seproject.inventory.dto.UserRegisterDto;
import com.seproject.inventory.entity.User;
import com.seproject.inventory.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register/{role}")
    public User register(@RequestBody UserRegisterDto dto,
                         @PathVariable String role) {
        return userService.registerUser(dto, role.toUpperCase());
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        if (auth.isAuthenticated()) {
            return "Login successful!";
        }
        return "Login failed!";
    }
}
