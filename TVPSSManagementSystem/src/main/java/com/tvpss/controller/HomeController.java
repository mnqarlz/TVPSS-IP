package com.tvpss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

	@GetMapping("/welcome")
    public String welcomePage(Model model) {
		model.addAttribute("TVPSS | Welcome", "Welcome Page");
        return "Welcome";
    }
	
	@GetMapping("/login/student")
	public String loginStudent(Model model) {
	    return "Auth/loginStudent";
	}
	
}
