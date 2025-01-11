package com.tvpss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SessionController {

    @GetMapping("/login/admin?expired")
    public String sessionExpired() {
        return "Auth/sessionExpired"; // Redirect to session expired page
    }
}
