package com.seproject.inventory.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/auth")
public class WebAuthController {

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";  // loads templates/auth/login.html
    }

    @GetMapping("/register")
    public String registerPage() {
        return "auth/register"; // loads templates/auth/register.html
    }
}
