package com.seproject.inventory.controller;

import com.seproject.inventory.entity.Role;
import com.seproject.inventory.entity.User;
import com.seproject.inventory.entity.Product;
import com.seproject.inventory.repository.OrderRepository;
import com.seproject.inventory.repository.ProductRepository;
import com.seproject.inventory.repository.RoleRepository;
import com.seproject.inventory.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
class InventoryControllerIntegrationTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    private User seller;
    private User buyer;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();

        orderRepository.deleteAll();
        productRepository.deleteAll();
        userRepository.deleteAll();

        Role sellerRole = roleRepository.findByName("SELLER")
                .orElseGet(() -> roleRepository.save(Role.builder().name("SELLER").build()));
        Role buyerRole = roleRepository.findByName("BUYER")
                .orElseGet(() -> roleRepository.save(Role.builder().name("BUYER").build()));

        seller = userRepository.save(User.builder()
                .username("seller_integration")
                .email("seller_integration@example.com")
                .password("encoded")
                .roles(Set.of(sellerRole))
                .build());

        buyer = userRepository.save(User.builder()
                .username("buyer_integration")
                .email("buyer_integration@example.com")
                .password("encoded")
                .roles(Set.of(buyerRole))
                .build());
    }

    @Test
    @WithMockUser(username = "buyer1", roles = {"BUYER"})
    void getProducts_ReturnsOk() throws Exception {
        Product product = new Product(null, "Laptop", "Gaming laptop", 5, 1200.0, seller);
        productRepository.save(product);

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"));
    }

    @Test
    @WithMockUser(username = "seller1", roles = {"SELLER"})
    void createProduct_AsSeller_ReturnsCreated() throws Exception {
        String payload = "{" +
                "\"name\":\"Monitor\"," +
                "\"description\":\"4K Monitor\"," +
                "\"price\":200.0," +
                "\"quantity\":8," +
                "\"sellerId\":" + seller.getId() +
                "}";

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Monitor"));
    }

    @Test
    @WithMockUser(username = "buyer2", roles = {"BUYER"})
    void placeOrder_AsBuyer_ReturnsCreated() throws Exception {
        Product product = productRepository.save(new Product(null, "SSD", "Fast SSD", 10, 99.0, seller));
        String payload = "{" +
                "\"buyerId\":" + buyer.getId() + "," +
                "\"productId\":" + product.getId() + "," +
                "\"quantity\":2" +
                "}";

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.quantity").value(2));
    }

    @Test
    @WithMockUser(username = "buyer1", roles = {"BUYER"})
    void createProduct_AsBuyer_ReturnsForbidden() throws Exception {
        String payload = "{" +
                "\"name\":\"Camera\"," +
                "\"price\":350.0," +
                "\"quantity\":3," +
                "\"sellerId\":" + seller.getId() +
                "}";

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(payload))
                .andExpect(status().isForbidden());
    }
}



