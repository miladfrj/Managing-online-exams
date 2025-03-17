package ir.maktabsharif.finalprojectphase12.service.impl;

import ir.maktabsharif.finalprojectphase12.dto.auth.UserResponseDTO;
import ir.maktabsharif.finalprojectphase12.entity.SearchCriteria;
import ir.maktabsharif.finalprojectphase12.entity.User;
import ir.maktabsharif.finalprojectphase12.entity.enums.ApprovalStatus;
import ir.maktabsharif.finalprojectphase12.entity.enums.Role;
import ir.maktabsharif.finalprojectphase12.exception.BadCredentialsException;
import ir.maktabsharif.finalprojectphase12.repository.UserRepository;
import ir.maktabsharif.finalprojectphase12.service.AdminService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserResponseDTO> searchUsers(SearchCriteria criteria) {
        List<User> users = userRepository.searchUsers(
                criteria.getRole(),
                criteria.getFirstname(),
                criteria.getLastname(),
                criteria.getApprovalStatus()
        );
        return users.stream().map(this::mapToResponse).collect(Collectors.toList());
    }


    // Approve a user
    @Override
    @Transactional  //if exception happen transaction roll back
    public void approveUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("User not found"));
        user.setApprovalStatus(ApprovalStatus.APPROVED);
        userRepository.save(user);
       // mapToResponse(user);
    }

    @Override
    public void updateUser(Long userId, UserResponseDTO userResponseDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("User not found"));
        user.setFirstName(userResponseDTO.getFirstName());
        user.setLastName(userResponseDTO.getLastName());
        user.setRole(userResponseDTO.getRole());
        userRepository.save(user);
        //mapToResponse(user);
    }

    @Override
    public UserResponseDTO  getUserById(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("User not found"));
        return mapToResponse(user);
    }

    @Override
    public void changeUserRole(Long userId, Role newRole) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BadCredentialsException("User not found"));
        user.setRole(newRole);
        userRepository.save(user);
       // mapToResponse(user);
    }



    @Override
    public UserResponseDTO mapToResponse(User user) {
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
}
