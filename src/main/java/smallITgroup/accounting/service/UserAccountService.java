package smallITgroup.accounting.service;

import java.util.List;

import smallITgroup.accounting.dto.UserDto;        // DTO for user data (email, roles)
import smallITgroup.accounting.dto.UserRegisterDto; // DTO for user registration data
import smallITgroup.accounting.dto.UserInfoDto;    // DTO for user info (including roles, password, etc.)

public interface UserAccountService {


    // Method to register a new user with the provided registration details.
    // The method returns a UserDto containing the registered user's details.
    UserDto register(UserRegisterDto userRegisterDto);

    // Method to retrieve the user based on their login (email).
    // The method returns a UserDto containing the user information.
    UserDto getUser(String login);

    // Method to remove an existing user based on their email.
    // The method returns a UserDto containing the deleted user's details.
    UserDto removeUser(String email);

    // Method to change the roles of a user.
    // 'role' can either be added or removed based on the 'isAddRole' parameter (true for adding, false for removing).
    // The method returns an updated UserDto with the new roles.
    UserDto changeRolesList(String email, String role, boolean isAddRole);

    // Method to change the password of a user.
    // It takes the user's email and the new password as input.
    // The method returns an updated UserInfoDto with the new password.
    UserInfoDto changePassword(String email, String newPassword);
    
    // Method to get a list of all users in the system.
    // The method returns a list of UserInfoDto containing information about all users.
    List<UserInfoDto> getAllUsers();
    
    // Method to initiate a password recovery for a user.
    // It takes the user's email and sends a recovery email with a new password.
    void recoveryPassword(String email);
  
  	List<String> getUsersEmail();

}
