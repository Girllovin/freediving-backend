package smallITgroup.accounting.dto;

import lombok.Getter;
import lombok.Setter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Getter
@Setter
public class LoginDto {

    @NotBlank(message = "Email can't be empty")
    @Email(message = "Invalid email format")
    String email;
    
    @NotBlank(message = "Password can't be empty")
    String password;
} 