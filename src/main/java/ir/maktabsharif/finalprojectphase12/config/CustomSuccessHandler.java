package ir.maktabsharif.finalprojectphase12.config;

import ir.maktabsharif.finalprojectphase12.entity.User;
import ir.maktabsharif.finalprojectphase12.entity.enums.ApprovalStatus;
import ir.maktabsharif.finalprojectphase12.exception.BadCredentialsException;
import ir.maktabsharif.finalprojectphase12.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.io.IOException;
import java.util.Set;

@Component
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    public CustomSuccessHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        String username = authentication.getName();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new BadCredentialsException("User not found"));

        if (user.getApprovalStatus() != ApprovalStatus.APPROVED) {
            response.sendRedirect("/user/not-approved");
            return;
        }

        if (roles.contains("ADMIN")) {
            response.sendRedirect("/admin/operation-selection");
        } else if (roles.contains("TEACHER")) {
            response.sendRedirect("/teacher/dashboard");
        } else if (roles.contains("STUDENT")) {
            response.sendRedirect("/student/dashboard");
        } else {
            response.sendRedirect("/home");
        }
    }
}
