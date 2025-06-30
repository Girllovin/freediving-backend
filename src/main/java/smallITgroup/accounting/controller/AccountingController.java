package smallITgroup.accounting.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import smallITgroup.accounting.dto.ChangePasswordDto;
import smallITgroup.accounting.dto.UserDto;
import smallITgroup.accounting.dto.UserInfoDto;
import smallITgroup.accounting.dto.UserRegisterDto;
import smallITgroup.accounting.service.UserAccountService;


@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountingController {


    // Injected service for user account operations
    final UserAccountService userAccountService;

    // Endpoint to register a new user
    @PostMapping("/register")
    public UserDto register(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        System.out.println("Begining of registration");
        return userAccountService.register(userRegisterDto);
    }

    // Endpoint to delete a user by email
    @DeleteMapping("/user/{email}")
    public UserDto removeUser(@PathVariable String email) {
        return userAccountService.removeUser(email);
    }

    // Endpoint to change password, allowed only if the email matches the authenticated user
    @PreAuthorize("#email == authentication.name")
    @PutMapping("/user/{email}/password")
    public UserInfoDto changePassword(
            @PathVariable String email,
            @RequestBody ChangePasswordDto changePasswordDto) {
        return userAccountService.changePassword(email, changePasswordDto.getNewPassword());
    }

    // Endpoint to get the list of all users
    @GetMapping("/users")
    public List<UserInfoDto> getAllUsers() {
        return userAccountService.getAllUsers();
    }

    // Endpoint to trigger password recovery process
    @GetMapping("/recovery/{email}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void getRecovery(@PathVariable String email) {
        userAccountService.recoveryPassword(email);
    }
    
    // Endpoint to get the list of all users email
    @GetMapping("/users/email")
    public List<String> getUsersEmail() {
        return userAccountService.getUsersEmail();
    }
}
