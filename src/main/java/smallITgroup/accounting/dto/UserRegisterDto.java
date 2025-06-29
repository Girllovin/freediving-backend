package smallITgroup.accounting.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

@Getter
@Setter
@Schema(description = "User registration request data")
public class UserRegisterDto {

	@Schema(description = "User's email address", example = "user@example.com", required = true)
	@NotBlank(message = "Email can't be empty")
    @Email(message = "Unavailable email")
	String email;
	
	@Schema(description = "User's password (minimum 6 characters)", example = "password123", required = true, minLength = 6)
	@NotBlank(message = "Password can't be empty")
    @Size(min = 6, message = "Password have to consist 6 symbols minimum")
	String password;

}
