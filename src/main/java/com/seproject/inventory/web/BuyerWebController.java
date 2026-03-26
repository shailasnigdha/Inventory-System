package com.seproject.inventory.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/web/buyer")
public class BuyerWebController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard/buyer-dashboard";
    }
}
