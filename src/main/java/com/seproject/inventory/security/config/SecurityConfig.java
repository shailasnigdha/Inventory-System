package com.seproject.inventory.security.config;

import com.seproject.inventory.security.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Allow Thymeleaf UI pages
                        .requestMatchers("/web/auth/**").permitAll()
                        .requestMatchers("/web/css/**", "/web/js/**", "/web/images/**").permitAll()

                        // API auth
                        .requestMatchers("/auth/**").permitAll()

                        // Role-based dashboards
                        .requestMatchers("/web/admin/**").hasRole("ADMIN")
                        .requestMatchers("/web/seller/**").hasRole("SELLER")
                        .requestMatchers("/web/buyer/**").hasRole("BUYER")

                        // API role protected endpoints
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/seller/**").hasRole("SELLER")
                        .requestMatchers("/buyer/**").hasRole("BUYER")

                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/web/auth/login")   // your custom login
                        .defaultSuccessUrl("/web/redirect", true)
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/web/auth/login?logout")
                        .permitAll()
                );

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
