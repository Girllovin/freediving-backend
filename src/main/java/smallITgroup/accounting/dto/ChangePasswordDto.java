package smallITgroup.accounting.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

// The @Getter annotation automatically generates getter methods for all fields in the class.
@Getter
public class ChangePasswordDto {

    // The @NotBlank annotation ensures that the password field is not null or empty.
    // The message parameter specifies the error message to return if the validation fails.
    @NotBlank(message = "Password can't be empty")
    
    // The @Size annotation ensures that the password has a minimum length of 6 characters.
    // If the length is less than 6, an error message is returned.
    @Size(min = 6, message = "Password have to consist 6 symbols minimum")
    String password;
    
    // The @NotBlank annotation ensures that the newPassword field is not null or empty.
    // The message parameter specifies the error message to return if the validation fails.
    @NotBlank(message = "Password can't be empty")
    
    // The @Size annotation ensures that the new password has a minimum length of 6 characters.
    // If the length is less than 6, an error message is returned.
    @Size(min = 6, message = "Password have to consist 6 symbols minimum")
    String newPassword;

}
