package com.tvpss.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.multipart.MultipartFile;
import com.tvpss.entity.SchoolInfo;
import com.tvpss.entity.TVPSSVersion;
import com.tvpss.entity.User;
import com.tvpss.enums.Role;
//import com.tvpss.service.FileService;
import com.tvpss.service.SchoolService;
import com.tvpss.service.TVPSSVersionService;
import com.tvpss.service.UserService;

@Controller
@RequestMapping("/4-SchoolAdmin")
public class SchoolAdminController {

    private final SchoolService schoolService;
    private final TVPSSVersionService tvpssVersionService;
    private final UserService userService;
    //private final FileService fileService;

    @Autowired
    public SchoolAdminController(SchoolService schoolService, TVPSSVersionService tvpssVersionService/*, FileService fileService*/, UserService userService) {
        this.schoolService = schoolService;
        this.tvpssVersionService = tvpssVersionService;
        this.userService = userService;
        //this.fileService = fileService;
    }

    @GetMapping("/dashboardScA")
    public String dashboardScA(Model model) {
        model.addAttribute("title", "School Admin Dashboard");
        return "4-SchoolAdmin/dashboardScA";
    }

    @GetMapping("/SchoolTVPSSVersion/updateSchoolInfo")
    public String showSchoolInfoForm(Model model) {
        SchoolInfo schoolInfo = schoolService.findSchoolInfo();
        model.addAttribute("schoolInfo", schoolInfo != null ? schoolInfo : new SchoolInfo());
        return "4-SchoolAdmin/SchoolTVPSSVersion/UpdateSchoolTVPSSVersion";
    }

    @PostMapping("/SchoolTVPSSVersion/saveSchoolInfo")
    public String saveSchoolInfo(SchoolInfo schoolInfo, Model model) {
        try {
            User existingUser = userService.findDefaultUserByRole(Role.SCHOOL_ADMIN);
            schoolInfo.setUser(existingUser); 

            System.out.println("DEBUG: School Info Object: " + schoolInfo);
            schoolService.saveOrUpdateSchoolInfo(schoolInfo);
            System.out.println("DEBUG: School information saved successfully.");
            return "redirect:/4-SchoolAdmin/SchoolTVPSSVersion/updateTVPSSVersion";
        } catch (Exception e) {
            System.out.println("ERROR: Exception while saving school info: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while saving school information.");
            return "redirect:/4-SchoolAdmin/SchoolTVPSSVersion/updateSchoolInfo";
        }
    }


    @GetMapping("/SchoolTVPSSVersion/updateTVPSSVersion")
    public String showTVPSSVersionForm(Model model) {
        try {
            SchoolInfo schoolInfo = schoolService.findSchoolInfo();
            if (schoolInfo == null) {
                return "redirect:/4-SchoolAdmin/SchoolTVPSSVersion/updateSchoolInfo";
            }

            TVPSSVersion tvpssVersion = tvpssVersionService.findOrCreateBySchoolInfo(schoolInfo);
            model.addAttribute("tvpssVersion", tvpssVersion);
            model.addAttribute("schoolInfoId", schoolInfo.getId()); // Add schoolInfoId to model
            return "4-SchoolAdmin/SchoolTVPSSVersion/UpdateSchoolTVPSSVersion2";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while loading the TVPSS version form.");
            return "redirect:/4-SchoolAdmin/dashboardScA";
        }
    }

    @PostMapping("/SchoolTVPSSVersion/saveTVPSSVersion")
    public String saveTVPSSVersion(
            @ModelAttribute TVPSSVersion tvpssVersion,
            @RequestParam Long schoolInfoId,  // Add this parameter
            Model model) {
        try {
            SchoolInfo schoolInfo = schoolService.findById(schoolInfoId);
            if (schoolInfo == null) {
                throw new IllegalArgumentException("No active school information found.");
            }

            tvpssVersion.setSchoolInfo(schoolInfo);
            tvpssVersionService.saveOrUpdateTVPSSVersion(tvpssVersion);

            return "redirect:/4-SchoolAdmin/dashboardScA";
        } catch (Exception e) {
            System.out.println("ERROR: Exception while saving TVPSSVersion: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while saving the TVPSS version.");
            return "redirect:/4-SchoolAdmin/SchoolTVPSSVersion/updateTVPSSVersion";
        }
    }


}
