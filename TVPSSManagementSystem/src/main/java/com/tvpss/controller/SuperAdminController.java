package com.tvpss.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

    @GetMapping("/dashboardSA")
    public String dashboardSA(Model model) {
        model.addAttribute("title", "Super Admin Dashboard");

        long stateAdminCount = userService.countUsersByRole(Role.STATE_ADMIN);
        long ppdAdminCount = userService.countUsersByRole(Role.PPD_ADMIN);
        long schoolAdminCount = userService.countUsersByRole(Role.SCHOOL_ADMIN);

        model.addAttribute("stateAdminCount", stateAdminCount);
        model.addAttribute("ppdAdminCount", ppdAdminCount);
        model.addAttribute("schoolAdminCount", schoolAdminCount);
        
        List<String> chartLabels = Arrays.asList("State Admin", "PPD Admin", "School Admin");
        List<Long> chartValues = Arrays.asList(stateAdminCount, ppdAdminCount, schoolAdminCount);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            model.addAttribute("chartLabels", objectMapper.writeValueAsString(chartLabels));
            model.addAttribute("chartValues", objectMapper.writeValueAsString(chartValues));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            model.addAttribute("chartLabels", "[]");
            model.addAttribute("chartValues", "[]");
        }

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

    @GetMapping("/UserManagement/addUser")
    public String addUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", Role.values());
        return "1-SuperAdmin/UserManagement/addUser";
    }

    @PostMapping("/UserManagement/saveUser")
    public String saveUser(@ModelAttribute("user") User user, Model model) {
        try {
            userService.saveUser(user);
            return "redirect:/1-SuperAdmin/UserManagement/listUsers";
        } catch (Exception e) {
            model.addAttribute("error", "An error occurred while saving the user. Please try again.");
            model.addAttribute("user", user);
            model.addAttribute("roles", Role.values());
            return "1-SuperAdmin/UserManagement/addUser";
        }
    }

    @GetMapping("/UserManagement/updateUser/{id}")
    public String showUpdateUserForm(@PathVariable Long id, Model model) {
        User user = userService.findUserById(id);
        if (user == null) {
            throw new IllegalArgumentException("Invalid user Id: " + id);
        }

        List<String> states = Arrays.asList(
            "Johor", "Melaka", "Pahang", "Selangor", "Negeri Sembilan",
            "Perak", "Kedah", "Pulau Pinang", "Perlis", "Kelantan",
            "Terengganu", "Sabah", "Sarawak",
            "Wilayah Persekutuan Kuala Lumpur", "Wilayah Persekutuan Labuan", "Wilayah Persekutuan Putrajaya"
        );

        Map<String, List<String>> districtsByState = new HashMap<>();
        districtsByState.put("Johor", Arrays.asList("Johor Bahru", "Muar", "Kluang", "Segamat", "Mersing", "Kota Tinggi", "Batu Pahat", "Pontian", "Pasir Gudang", "Tangkak", "Kulaijaya"));
        districtsByState.put("Melaka", Arrays.asList("Melaka Tengah", "Alor Gajah", "Jasin"));
        districtsByState.put("Pahang", Arrays.asList("Kuantan", "Temerloh", "Bera", "Pekan", "Rompin", "Jerantut", "Bentong", "Maran", "Lipis", "Raub", "Cameron Highlands"));
        districtsByState.put("Selangor", Arrays.asList("Petaling", "Hulu Langat", "Sepang", "Klang", "Gombak", "Kuala Selangor", "Sabak Bernam", "Selayang", "Shah Alam", "Subang Jaya"));
        districtsByState.put("Negeri Sembilan", Arrays.asList("Seremban", "Port Dickson", "Rembau", "Jelebu", "Tampin", "Gemenceh", "Kuala Pilah", "Jempol", "Bahau"));
        districtsByState.put("Perak", Arrays.asList("Ipoh", "Kuala Kangsar", "Taiping", "Teluk Intan", "Sitiawan", "Parit Buntar", "Tanjung Malim", "Kampar", "Manjung", "Batu Gajah"));
        districtsByState.put("Kedah", Arrays.asList("Alor Setar", "Sungai Petani", "Kuala Kedah", "Kulim", "Baling", "Langkawi", "Pokok Sena", "Kubang Pasu", "Pendang", "Yan", "Bandar Baharu"));
        districtsByState.put("Pulau Pinang", Arrays.asList("George Town", "Bukit Mertajam", "Nibong Tebal", "Balik Pulau", "Seberang Perai Utara", "Seberang Perai Tengah", "Seberang Perai Selatan"));
        districtsByState.put("Perlis", Arrays.asList("Kangar", "Arau", "Padang Besar"));
        districtsByState.put("Kelantan", Arrays.asList("Kota Bharu", "Tumpat", "Pasir Mas", "Machang", "Tanah Merah", "Gua Musang", "Kuala Krai", "Bachok", "Jeli", "Pasir Puteh"));
        districtsByState.put("Terengganu", Arrays.asList("Kuala Terengganu", "Dungun", "Kemaman", "Besut", "Hulu Terengganu", "Marang", "Setiu"));
        districtsByState.put("Sabah", Arrays.asList("Kota Kinabalu", "Sandakan", "Tawau", "Keningau", "Beaufort", "Lahad Datu", "Semporna", "Ranau", "Papar", "Tuaran", "Penampang", "Kudat"));
        districtsByState.put("Sarawak", Arrays.asList("Kuching", "Sibu", "Miri", "Bintulu", "Sri Aman", "Mukah", "Betong", "Limbang", "Sarikei", "Kapit", "Lawas", "Samarahan"));
        districtsByState.put("Wilayah Persekutuan Kuala Lumpur", Arrays.asList("Kuala Lumpur"));
        districtsByState.put("Wilayah Persekutuan Labuan", Arrays.asList("Labuan"));
        districtsByState.put("Wilayah Persekutuan Putrajaya", Arrays.asList("Putrajaya"));
        districtsByState.put("Melaka", Arrays.asList("Melaka Tengah","Jasin","Alor Gajah"));
        
        List<String> districts = districtsByState.getOrDefault(user.getState(), Collections.emptyList());

        model.addAttribute("states", states);
        model.addAttribute("districts", districts);
        model.addAttribute("user", user);
        return "1-SuperAdmin/UserManagement/UpdateUser";
    }

    @PostMapping("/UserManagement/updateSaveUser")
    public String updateUser(@ModelAttribute User user) {
        System.out.println("Received User ID: " + user.getId());
        System.out.println("Received Name: " + user.getName());
        System.out.println("Received Email: " + user.getEmail());
        System.out.println("Received Role: " + user.getRole());
        System.out.println("Received State: " + user.getState());
        System.out.println("Received District: " + user.getDistrict());
        System.out.println("Received Password: " + user.getPassword());

        User existingUser = userService.findUserById(user.getId());
        if (existingUser == null) {
            throw new IllegalArgumentException("Invalid user ID: " + user.getId());
        }

        existingUser.setName(user.getName());
        existingUser.setEmail(user.getEmail());
        existingUser.setState(user.getState());
        existingUser.setDistrict(user.getDistrict());

        if (user.getRole() != null) {
            existingUser.setRole(Role.valueOf(user.getRole().toString())); 
        }

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(user.getPassword());
        }

        userService.updateUser(existingUser);

        return "redirect:/1-SuperAdmin/UserManagement/listUsers";
    }

    @GetMapping("/UserManagement/deleteUser/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUserById(id);
        return "redirect:/1-SuperAdmin/UserManagement/listUsers";
    }
}
