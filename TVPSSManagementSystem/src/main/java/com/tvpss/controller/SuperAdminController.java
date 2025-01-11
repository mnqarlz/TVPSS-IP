package com.tvpss.controller;

import com.tvpss.entity.User;
import com.tvpss.enums.Role;
import com.tvpss.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/1-SuperAdmin")
public class SuperAdminController {

    private final UserService userService;

    @Autowired
    public SuperAdminController(UserService userService) {
        this.userService = userService;
    }

    // Dashboard Route
    @GetMapping("/dashboardSA")
    public String dashboardSA(Model model) {
        model.addAttribute("title", "Super Admin Dashboard");
        return "1-SuperAdmin/dashboardSA";
    }

    @GetMapping("/UserManagement/listUsers")
    public String listUsers(@RequestParam(value = "role", required = false) Role role,
                            Model model) {
        List<User> users;
        if (role != null) {
            users = userService.findUsersByRole(role);
        } else {
            users = userService.findAllUsers();
        }

        // Add role color logic
        for (User user : users) {
            switch (user.getRole().name()) {
                case "SUPER_ADMIN":
                    user.setRoleColor("bg-blue-500");
                    break;
                case "STATE_ADMIN":
                    user.setRoleColor("bg-green-500");
                    break;
                case "PPD_ADMIN":
                    user.setRoleColor("bg-yellow-500");
                    break;
                case "SCHOOL_ADMIN":
                    user.setRoleColor("bg-red-500");
                    break;
                default:
                    user.setRoleColor("bg-gray-500");
            }
        }

        model.addAttribute("users", users);
        model.addAttribute("selectedRole", role);
        return "1-SuperAdmin/UserManagement/ListUser";
    }


    // Add User Route
    @GetMapping("/UserManagement/addUser")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return "1-SuperAdmin/UserManagement/addUser";
    }

    @PostMapping("/UserManagement/addUser")
    public String saveUser(@ModelAttribute("user") User user,
                           Model model) {
        userService.saveUser(user);
        return "redirect:/1-SuperAdmin/UserManagement/listUsers";
    }

    // Update User Route
    @GetMapping("/UserManagement/updateUser/{id}")
    public String updateUserForm(@PathVariable Long id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "1-SuperAdmin/UserManagement/UpdateUser";
    }

    @PostMapping("/UserManagement/updateUser/{id}")
    public String updateUser(@PathVariable Long id, @ModelAttribute("user") User user) {
        user.setId(id);
        userService.updateUser(user);
        return "redirect:/1-SuperAdmin/UserManagement/listUsers";
    }

    // Delete User Route
    @GetMapping("/UserManagement/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/1-SuperAdmin/UserManagement/listUsers";
    }
}
