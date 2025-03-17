package ir.maktabsharif.finalprojectphase12.service;

import ir.maktabsharif.finalprojectphase12.dto.auth.LoginRequestDTO;
import ir.maktabsharif.finalprojectphase12.dto.auth.SignUpRequestDTO;
import ir.maktabsharif.finalprojectphase12.entity.User;
import jakarta.validation.constraints.NotNull;

import java.util.List;


public interface UserService {

    void registerUser(SignUpRequestDTO signUpRequestDTO);

    List<User> getAllTeachers();

    void loginUser(LoginRequestDTO loginRequestDTO);

    List<User> getAllUsers();

}
