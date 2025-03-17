package ir.maktabsharif.finalprojectphase12.utility;

import ir.maktabsharif.finalprojectphase12.entity.User;
import ir.maktabsharif.finalprojectphase12.exception.BadCredentialsException;
import ir.maktabsharif.finalprojectphase12.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UserContext {
    private final UserRepository userRepository;

    @Autowired
    public UserContext(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long getLoggedInUserId() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String username = ((UserDetails) principal).getUsername();
            return findUserIdByUsername(username);
        }
        throw new BadCredentialsException("User not authenticated");
    }

    public User getLoggedInUser() {
        return userRepository.findById(getLoggedInUserId())
                .orElseThrow(() -> new BadCredentialsException("User not found"));
    }

    private Long findUserIdByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("User not found"))
                .getId();
    }
}
