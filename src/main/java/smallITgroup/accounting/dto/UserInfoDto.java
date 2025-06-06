package smallITgroup.accounting.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Singular;

// @Getter annotation automatically generates getter methods for all fields in the class.
@Getter

// @AllArgsConstructor generates a constructor with one parameter for each field in the class.
@AllArgsConstructor

// @NoArgsConstructor generates a no-arguments constructor, useful for frameworks like JPA or Jackson.
@NoArgsConstructor

// @Builder is used to implement the builder pattern, allowing for easier construction of UserInfoDto objects.
@Builder
public class UserInfoDto {

    // Represents the email of the user.
    String email;
    
    // Represents the user's password.
    String password;
    
    // The @Singular annotation allows for the creation of a single element in a Set using the builder.
    // This enables adding individual roles without requiring the entire collection upfront.
    @Singular
    Set<String> roles;

}
