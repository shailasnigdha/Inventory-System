package com.seproject.inventory.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.seproject.inventory.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "SELLER")
    void createProduct() throws Exception {
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(10.0);
        product.setQuantity(5);

        mockMvc.perform(post("/products/create/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Product"));
    }

    @Test
    @WithMockUser(roles = "SELLER")
    void updateProduct() throws Exception {
        // First create
        Product product = new Product();
        product.setName("Test Product");
        product.setPrice(10.0);
        product.setQuantity(5);

        String response = mockMvc.perform(post("/products/create/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andReturn().getResponse().getContentAsString();

        Product created = objectMapper.readValue(response, Product.class);

        // Update
        product.setName("Updated Product");
        mockMvc.perform(put("/products/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Product"));
    }

    @Test
    void getAllProducts() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk());
    }

    @Test
    void getProductById() throws Exception {
        // Assuming product exists, but for test, may need to create first
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk());
    }
}
