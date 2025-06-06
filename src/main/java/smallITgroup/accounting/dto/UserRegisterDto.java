package smallITgroup.accounting.dto;

import lombok.Getter;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Getter
public class UserRegisterDto {

	@NotBlank(message = "Email can't be empty")
    @Email(message = "Unawaillable email")
	String email;
	
	@NotBlank(message = "Password can't be empty")
    @Size(min = 6, message = "Password have to consist 6 symbols minimum")
	String password;

}
