package com.seproject.inventory.web;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web")
public class WebRedirectController {

    @GetMapping("/redirect")
    public String redirectAfterLogin(Authentication authentication) {

        if (authentication == null) {
            return "redirect:/web/auth/login";
        }

        // 🔵 If user is ADMIN → send to admin dashboard
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/web/admin/dashboard";
        }

        // 🟠 If user is SELLER → send to seller dashboard
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_SELLER"))) {
            return "redirect:/web/seller/dashboard";
        }

        // 🟢 Default fallback → BUYER dashboard
        return "redirect:/web/buyer/dashboard";
    }
}
