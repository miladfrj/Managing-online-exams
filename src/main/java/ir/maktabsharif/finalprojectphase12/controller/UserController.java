package ir.maktabsharif.finalprojectphase12.controller;

import ir.maktabsharif.finalprojectphase12.dto.auth.LoginRequestDTO;
import ir.maktabsharif.finalprojectphase12.dto.auth.SignUpRequestDTO;
import ir.maktabsharif.finalprojectphase12.exception.BadCredentialsException;
import ir.maktabsharif.finalprojectphase12.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/not-approved")
    public String notApprovedPage(Model model) {
        model.addAttribute("message",
                "Your Account is not yet approved. Please contact the administrator for more information.");
        return "user/not-approved";
    }

    @GetMapping("/signup")
    public String showRegistrationForm(Model model) {
        model.addAttribute("signUpRequestDto", new SignUpRequestDTO());
        return "user/signup";
    }

    @PostMapping("/signup")
    public String registerUser(@Valid @ModelAttribute("signUpRequestDto") SignUpRequestDTO requestDTO,
           BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "user/signup";
        }
        try {
            userService.registerUser(requestDTO);
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "user/signup";
        }
        return "redirect:/user/registration-success"; // Redirect to success page
    }

    @GetMapping("/registration-success")
    public String registrationSuccess() {
        return "user/registration-success";
    }

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("errorMessage", "Invalid credentials, please try again.");
        }
        return "user/login";
    }


    @PostMapping("/login")
    public String loginUser(@RequestParam String username, @RequestParam String password, Model model) {
        try {
            // Create LoginRequestDTO and set values
            LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
            loginRequestDTO.setUsername(username);
            loginRequestDTO.setPassword(password);

            // Pass loginRequestDTO to the service method
            userService.loginUser(loginRequestDTO);

            return "redirect:/home";
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", "Invalid username or password.");
            return "user/login";
        }
    }

}
