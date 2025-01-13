package com.tvpss.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

//import com.tvpss.service.SchoolService;

@Controller
@RequestMapping("/4-SchoolAdmin")
public class SchoolAdminController {

    @GetMapping("/dashboardScA")
    public String dashboardScA(Model model) {
    	System.out.println("Dashboard route accessed");
    	model.addAttribute("title", "School Admin Dashboard");
        return "4-SchoolAdmin/dashboardScA";
    }
}
