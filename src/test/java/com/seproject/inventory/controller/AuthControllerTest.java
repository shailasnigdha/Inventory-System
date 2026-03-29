package com.seproject.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seproject.inventory.dto.UserRegisterDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void registerUser() throws Exception {
        UserRegisterDto dto = new UserRegisterDto();
        dto.setUsername("testuser");
        dto.setEmail("test@example.com");
        dto.setPassword("password");

        mockMvc.perform(post("/auth/register/BUYER")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void login() throws Exception {
        // First register
        UserRegisterDto dto = new UserRegisterDto();
        dto.setUsername("loginuser");
        dto.setEmail("login@example.com");
        dto.setPassword("password");

        mockMvc.perform(post("/auth/register/BUYER")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        // Then login
        mockMvc.perform(post("/auth/login")
                        .param("username", "loginuser")
                        .param("password", "password"))
                .andExpect(status().isOk())
                .andExpect(content().string("Login successful!"));
    }
}
