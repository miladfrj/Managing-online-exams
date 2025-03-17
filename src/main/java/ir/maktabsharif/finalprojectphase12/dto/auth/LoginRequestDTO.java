package ir.maktabsharif.finalprojectphase12.dto.auth;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDTO {
    @NotBlank(message = "username must insert")
    @Size(min = 5 , max = 10 , message = "Username must be between 5 and 10 characters")
    private String username;
    @NotBlank(message = "password must insert")
    @Size(min = 5 , max = 50 , message = "password must be between 5 and 100 characters")
    private String password;
}
