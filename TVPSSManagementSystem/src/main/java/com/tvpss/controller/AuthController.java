package com.tvpss.controller;

import com.tvpss.entity.User;
import com.tvpss.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final UserService userService;

    //@Autowired
    public AuthController(UserService userService) {
        this.userService = userService; 
    }

    @GetMapping("/login/admin")
    public String showLoginForm() {
        return "Auth/loginAdmin";
    }

    @PostMapping("/login")
    public String authenticateUser(@RequestParam("email") String email,
                                   @RequestParam("password") String password,
                                   Model model) {
        try {
            System.out.println("Email received: " + email);

            User user = userService.findUserByEmail(email);
            System.out.println("User retrieved: " + user);

            if (user == null) {
                model.addAttribute("error", "No user found for the provided email.");
                return "Auth/loginAdmin";
            }

            System.out.println("Raw password: " + password);
            System.out.println("Encoded password in DB: " + user.getPassword());
            System.out.println("Password match: " + userService.matchesPassword(password, user.getPassword()));

            if (!userService.matchesPassword(password, user.getPassword())) {
                model.addAttribute("error", "Invalid password.");
                return "Auth/loginAdmin";
            }

            System.out.println("Determining dashboard for role: " + user.getRole());
            String dashboard = userService.determineDashboard(user.getRole());
            System.out.println("Redirecting to: " + dashboard);

            return "redirect:" + dashboard;
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An unexpected error occurred. Please try again.");
            return "Auth/loginAdmin";
        }
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/login/admin?logout";
    }
}