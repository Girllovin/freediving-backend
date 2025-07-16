package smallITgroup.accounting.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import smallITgroup.accounting.dto.ChangePasswordDto;
import smallITgroup.accounting.dto.LoginDto;
import smallITgroup.accounting.dto.UserDto;
import smallITgroup.accounting.dto.UserInfoDto;
import smallITgroup.accounting.dto.UserRegisterDto;
import smallITgroup.accounting.service.UserAccountService;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AccountingController {

    // Injected service for user account operations
    final UserAccountService userAccountService;

    @PostMapping("/account/register")
    public UserDto register(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        System.out.println("Begining of registration");
        return userAccountService.register(userRegisterDto);
    }

    @PostMapping("/account/login")
    public UserDto login(@Valid @RequestBody LoginDto loginDto) {
        System.out.println("Beginning of login");
        return userAccountService.login(loginDto);
    }

    @DeleteMapping("/account/user/{email}")
    public UserDto removeUser(@PathVariable String email) {
        return userAccountService.removeUser(email);
    }

    @PreAuthorize("#email == authentication.name")
    @PutMapping("/account/user/{email}/password")
    public UserInfoDto changePassword(
            @PathVariable String email,
            @RequestBody ChangePasswordDto changePasswordDto) {
        return userAccountService.changePassword(email, changePasswordDto.getNewPassword());
    }

    @GetMapping("/account/users")
    public List<UserInfoDto> getAllUsers() {
        return userAccountService.getAllUsers();
    }

    @GetMapping("/account/recovery/{email}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void getRecovery(@PathVariable String email) {
        userAccountService.recoveryPassword(email);
    }
    
    @GetMapping("/account/users/email")
    public List<String> getUsersEmail() {
        return userAccountService.getUsersEmail();
    }
}
