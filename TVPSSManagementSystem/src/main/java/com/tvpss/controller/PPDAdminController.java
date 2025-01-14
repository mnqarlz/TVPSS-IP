package com.tvpss.controller;

import com.tvpss.entity.TVPSSVersion;
import com.tvpss.enums.ApprovalStatus;
import com.tvpss.enums.Role;
import com.tvpss.service.TVPSSVersionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/3-PPDAdmin")
public class PPDAdminController {

    private final TVPSSVersionService tvpssVersionService;

    @Autowired
    public PPDAdminController(TVPSSVersionService tvpssVersionService) {
        this.tvpssVersionService = tvpssVersionService;
    }

    /**
     * Display the PPD Admin dashboard.
     */
    @GetMapping("/dashboardPPD")
    public String dashboardPPD(Model model) {
        model.addAttribute("title", "PPD Admin Dashboard");
        return "3-PPDAdmin/dashboardPPD";
    }

    /**
     * List all pending TVPSS versions for review.
     */
    @GetMapping("/SchoolVersionStatus/listSchoolPPD")
    public String listPendingSchoolVersions(Model model) {
        List<TVPSSVersion> pendingVersions = tvpssVersionService.findAllByStatus(ApprovalStatus.PENDING);
        model.addAttribute("pendingVersions", pendingVersions);
        return "3-PPDAdmin/SchoolVersionStatus/listSchoolPPD";
    }

    /**
     * Display the review page for a specific TVPSS version.
     */
    @GetMapping("/SchoolVersionStatus/updateTVPSSPPD/{id}")
    public String reviewSchoolVersion(@PathVariable Long id, Model model) {
        TVPSSVersion version = tvpssVersionService.findById(id);
        if (version == null) {
            model.addAttribute("error", "TVPSS Version not found.");
            return "redirect:/3-PPDAdmin/SchoolVersionStatus/listSchoolPPD";
        }
        model.addAttribute("version", version);
        return "3-PPDAdmin/SchoolVersionStatus/updateTVPSSPPD";
    }

    /**
     * Handle approval or rejection of a school version.
     */
    @PostMapping("/SchoolVersionStatus/updateApprovalStatus")
    public String updateApprovalStatus(
            @RequestParam("id") Long id,
            @RequestParam("isApproved") boolean isApproved,
            Model model) {
        try {
            tvpssVersionService.updateApprovalStatus(id, isApproved, Role.PPD_ADMIN);
            return "redirect:/3-PPDAdmin/SchoolVersionStatus/listSchoolPPD";
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            return "redirect:/3-PPDAdmin/SchoolVersionStatus/listSchoolPPD";
        }
    }

    /**
     * View details of approved TVPSS versions.
     */
    @GetMapping("/SchoolVersionStatus/approvedVersions")
    public String listApprovedVersions(Model model) {
        List<TVPSSVersion> approvedVersions = tvpssVersionService.findAllByStatus(ApprovalStatus.APPROVED);
        model.addAttribute("approvedVersions", approvedVersions);
        return "3-PPDAdmin/SchoolVersionStatus/approvedVersions";
    }
}
