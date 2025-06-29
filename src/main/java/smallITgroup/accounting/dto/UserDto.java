package smallITgroup.accounting.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;
import io.swagger.v3.oas.annotations.media.Schema;

// @Getter annotation automatically generates getter methods for all fields in the class.
@Getter

// @AllArgsConstructor generates a constructor with one parameter for each field in the class.
@AllArgsConstructor

// @NoArgsConstructor generates a no-arguments constructor.
@NoArgsConstructor

// @Builder is used to implement the builder pattern, allowing for easier construction of UserDto objects.
@Builder
@Schema(description = "User data transfer object")
public class UserDto {

    // Represents the email of the user.
    @Schema(description = "User's email address", example = "user@example.com")
    String email;
    
    // The @Singular annotation allows for the creation of a single element in a Set using the builder.
    // This allows us to add individual roles with the builder without using the entire collection.
    @Singular
    @Schema(description = "User's roles", example = "[\"USER\", \"ADMIN\"]")
    Set<String> roles;
}
