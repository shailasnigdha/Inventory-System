package com.seproject.inventory.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/seller")
public class SellerWebController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard/seller-dashboard";
    }
}

