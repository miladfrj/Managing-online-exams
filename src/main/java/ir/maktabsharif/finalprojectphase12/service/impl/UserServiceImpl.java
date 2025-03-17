package ir.maktabsharif.finalprojectphase12.service.impl;

import ir.maktabsharif.finalprojectphase12.dto.auth.LoginRequestDTO;
import ir.maktabsharif.finalprojectphase12.dto.auth.SignUpRequestDTO;
import ir.maktabsharif.finalprojectphase12.dto.auth.UserResponseDTO;
import ir.maktabsharif.finalprojectphase12.entity.User;
import ir.maktabsharif.finalprojectphase12.entity.enums.ApprovalStatus;
import ir.maktabsharif.finalprojectphase12.entity.enums.Role;
import ir.maktabsharif.finalprojectphase12.exception.BadCredentialsException;
import ir.maktabsharif.finalprojectphase12.repository.UserRepository;
import ir.maktabsharif.finalprojectphase12.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
//we dont write constructors manually
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(SignUpRequestDTO requestDTO) {
        if (userRepository.existsByUsername(requestDTO.getUsername())) {
            throw new BadCredentialsException("Username already exists. Please choose a different username.");
        }
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            throw new BadCredentialsException("Email already exists. Please use a different email.");
        }

        User user = User.builder()
                .username(requestDTO.getUsername())
                .password(passwordEncoder.encode(requestDTO.getPassword()))
                .email(requestDTO.getEmail())
                .firstName(requestDTO.getFirstName())
                .lastName(requestDTO.getLastName())
                .role(requestDTO.getRole())
                .approvalStatus(ApprovalStatus.PENDING)
                .build();
        userRepository.save(user);

//User savedUser =
     //   mapToResponse(savedUser);
    }

    @Override
    public List<User> getAllTeachers() {
        return userRepository.findByRole(Role.TEACHER);
    }

    @Override
    public void loginUser(LoginRequestDTO requestDTO) {
        Optional<User> userOptional = userRepository.findByUsername(requestDTO.getUsername());

        if (userOptional.isEmpty() || !passwordEncoder.matches(requestDTO.getPassword(), userOptional.get().getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        mapToResponse(userOptional.get());
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    //convert user to user response dto
    private UserResponseDTO mapToResponse(User user) {
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .role(user.getRole())
                .approvalStatus(user.getApprovalStatus())
                .build();
    }

    //@Transactional
    //public void approveUser(Long userId) {
    //    User user = userRepository.findById(userId)
    //            .orElseThrow(() -> new BadCredentialsException("User not found"));
    //    user.setApprovalStatus(ApprovalStatus.APPROVED);
    //    userRepository.save(user);
    //}
}
