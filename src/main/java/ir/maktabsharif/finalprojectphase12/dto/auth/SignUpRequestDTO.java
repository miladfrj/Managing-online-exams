package ir.maktabsharif.finalprojectphase12.dto.auth;

import ir.maktabsharif.finalprojectphase12.entity.enums.Role;
import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpRequestDTO {
    @NotBlank(message = "username must insert")
    @Size(min = 5 , max = 10 , message = "Username must be between 5 and 10 characters")
    private String username;
    @NotBlank(message = "password must insert")
    @Size(min = 8 , max = 50 , message = "password must be between 5 and 100 characters")
    @Pattern(
            regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
            message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character, and no whitespace"
    )
    private String password;
    @NotBlank(message = "Email must insert")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Firstname must insert")
    @Pattern(regexp = "^[A-Za-z]+$", message = "First name must contain only letters")
    private String firstName;
    @NotBlank(message = "Lastname must insert")
    @Pattern(regexp = "^[A-Za-z]+$", message = "Last name must contain only letters")
    private String lastName;
    @NotNull(message = "Role must be provided")
    private Role role;
}
