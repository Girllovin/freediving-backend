package smallITgroup.accounting.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Schema(description = "User login request data")
public class LoginDto {

    @Schema(description = "User's email address", example = "user@example.com", required = true)
    @NotBlank(message = "Email can't be empty")
    @Email(message = "Invalid email format")
    String email;
    
    @Schema(description = "User's password", example = "password123", required = true)
    @NotBlank(message = "Password can't be empty")
    String password;
} 