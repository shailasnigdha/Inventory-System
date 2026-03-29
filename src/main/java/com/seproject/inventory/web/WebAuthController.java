package com.seproject.inventory.web;

import com.seproject.inventory.dto.UserRegisterDto;
import com.seproject.inventory.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/web/auth")
@RequiredArgsConstructor
public class WebAuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute UserRegisterDto dto,
                               @RequestParam(defaultValue = "BUYER") String role) {
        userService.registerUser(dto, role.toUpperCase());
        return "redirect:/web/auth/login?registered";
    }
}
