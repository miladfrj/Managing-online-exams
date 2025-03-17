package ir.maktabsharif.finalprojectphase12.controller;

import ir.maktabsharif.finalprojectphase12.dto.auth.UserResponseDTO;
import ir.maktabsharif.finalprojectphase12.entity.SearchCriteria;
import ir.maktabsharif.finalprojectphase12.entity.enums.Role;
import ir.maktabsharif.finalprojectphase12.exception.BadCredentialsException;
import ir.maktabsharif.finalprojectphase12.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<UserResponseDTO> users = adminService.getAllUsers();
        model.addAttribute("users", users);
        return "admin/admin-dashboard";
    }

    @PostMapping("/approve/{userId}")
    public String approve(@PathVariable Long userId , Model model) {
        try {
            adminService.approveUser(userId);
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/admin-dashboard";
        }
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/edit/{userId}")
    public String getEditUserPage(@PathVariable Long userId, Model model) {
        try {
            UserResponseDTO user = adminService.getUserById(userId);
            model.addAttribute("user", user);
        }catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/admin-dashboard";
        }
        return "admin/admin-edit-user";
    }

//    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @PostMapping("/edit/{userId}")
    public String editUser(@PathVariable Long userId, @ModelAttribute UserResponseDTO user , Model model) {
        try {
            adminService.updateUser(userId, user);
        }catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/admin-edit-user";
        }
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/change-role/{userId}")
    public String changeRole(@PathVariable Long userId, @RequestParam Role role , Model model) {
        try {
            adminService.changeUserRole(userId, role);
        }catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "admin/admin-dashboard";
        }
        return "redirect:/admin/dashboard";
    }

    @GetMapping("/search")
    public String searchUsers(@ModelAttribute SearchCriteria criteria, Model model) {
        List<UserResponseDTO> users = adminService.searchUsers(criteria);
        model.addAttribute("users", users);
        return "admin/user-list";
    }


    @GetMapping("/operation-selection")
    public String ShowOperationSelectionPage() {
        return "admin/operation-selection";
    }
}

