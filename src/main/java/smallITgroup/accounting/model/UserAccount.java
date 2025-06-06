package smallITgroup.accounting.model;

import java.util.HashSet;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Document(collection = "AccessUsers") // This annotation marks the class as a MongoDB document. 
@ToString // Lombok annotation to automatically generate a toString() method
public class UserAccount {
    
    @Id // This annotation marks the 'email' field as the unique identifier (primary key) in MongoDB.
    String email;
    
    @Setter // Lombok annotation to generate the setter for 'password'.
    String password;
    
    Set<String> roles; // A set to store the roles associated with the user (e.g., "ADMIN", "USER").
    
    // Default constructor initializing 'roles' to an empty HashSet.
    public UserAccount() {
        roles = new HashSet<>();
    }

    // Constructor that initializes 'email' and 'password', and calls the default constructor to initialize 'roles'.
    public UserAccount(String email, String password) {
        this(); // Calls the default constructor to initialize 'roles'.
        this.email = email;
        this.password = password;
    }

    // Method to add a role to the user.
    public boolean addRole(String role) {
        return roles.add(role); // Adds the role to the set and returns whether the addition was successful.
    }

    // Method to remove a role from the user.
    public boolean removeRole(String role) {
        return roles.remove(role); // Removes the role from the set and returns whether the removal was successful.
    }
}
