package smallITgroup.accounting.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import smallITgroup.accounting.dto.ChangePasswordDto;
import smallITgroup.accounting.dto.LoginDto;
import smallITgroup.accounting.dto.UserDto;
import smallITgroup.accounting.dto.UserInfoDto;
import smallITgroup.accounting.dto.UserRegisterDto;
import smallITgroup.accounting.service.UserAccountService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AccountingController {

    // Injected service for user account operations
    final UserAccountService userAccountService;
    final AuthenticationManager authenticationManager;
    
    @GetMapping("/account/check")
    public AuthCheckResponse checkAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAuth = auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getName());

        return new AuthCheckResponse(isAuth);
    }

    static class AuthCheckResponse {
        public boolean isAuth;

        public AuthCheckResponse(boolean isAuth) {
            this.isAuth = isAuth;
        }
    }

    @PostMapping("/account/register")
    public UserDto register(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        System.out.println("Begining of registration");
        return userAccountService.register(userRegisterDto);
    }

    @PostMapping("/account/login")
    public UserDto login(@Valid @RequestBody LoginDto loginDto, HttpServletRequest request) {
        System.out.println("Beginning of login");

        // Выполняем аутентификацию
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        // Сохраняем аутентификацию в SecurityContext
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Создаём сессию и сохраняем туда SecurityContext
        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        // Возвращаем данные пользователя, например, из сервиса
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
    	System.out.println("Begining of recovery");
        userAccountService.recoveryPassword(email);
    }
    
    @GetMapping("/account/users/email")
    public List<String> getUsersEmail() {
        return userAccountService.getUsersEmail();
    }
}
