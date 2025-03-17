package ir.maktabsharif.finalprojectphase12.service;

import ir.maktabsharif.finalprojectphase12.dto.auth.UserResponseDTO;
import ir.maktabsharif.finalprojectphase12.entity.SearchCriteria;
import ir.maktabsharif.finalprojectphase12.entity.User;
import ir.maktabsharif.finalprojectphase12.entity.enums.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AdminService {

    List<UserResponseDTO> getAllUsers();
    //search users(criteria)
    List<UserResponseDTO> searchUsers(SearchCriteria criteria);
    // Approve a user
    void approveUser(Long userId);
    // Update user info (change role or other properties)
    void updateUser(Long userId, UserResponseDTO userResponseDTO);
    UserResponseDTO getUserById(Long userid);
    void changeUserRole(Long userId, Role newRole);
    UserResponseDTO mapToResponse(User user);
}
