package smallITgroup.accounting.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import smallITgroup.accounting.dto.ChangePasswordDto;
import smallITgroup.accounting.dto.LoginDto;
import smallITgroup.accounting.dto.LoginResponse;
import smallITgroup.accounting.dto.UserDto;
import smallITgroup.accounting.dto.UserInfoDto;
import smallITgroup.accounting.dto.UserRegisterDto;
import smallITgroup.accounting.model.UserAccount;
import smallITgroup.accounting.service.UserAccountService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import smallITgroup.accounting.dao.UserAccountRepository;
import smallITgroup.accounting.dto.exeptions.UserNotFoundException;
import smallITgroup.payment.service.PaymentService;
import smallITgroup.security.JwtService;


@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
public class AccountingController {
    private final JwtService jwtService;
    private final UserAccountRepository userAccountRepository;
    private final PaymentService paymentService;

    // Injected service for user account operations
    final UserAccountService userAccountService;
    final AuthenticationManager authenticationManager;
    
    @GetMapping("/account/check")
    public ResponseEntity<?> checkAuth(@RequestHeader(value = "Authorization", required = false) String token) {
        if (token == null || token.isBlank()) {
            return ResponseEntity.ok(new AuthCheckResponse(false));
        }
        try {
            String jwt = token.replace("Bearer ", "").trim();
            String email = jwtService.extractEmail(jwt);
            if (email == null || email.isEmpty()) {
                return ResponseEntity.ok(new AuthCheckResponse(false));
            }
            UserAccount user = userAccountRepository.findById(email)
                    .orElseThrow(UserNotFoundException::new);
            // Check user access
            boolean hasAccess = paymentService.hasCompletedPayment(email);
            if (!hasAccess) {
            	return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                        new AuthCheckFullResponse(
                            true,
                            new UserDto(user.getEmail(), user.getRoles()),
                            false
                        )
                    );
            }
            return ResponseEntity.ok(new AuthCheckFullResponse(
                    true,
                    new UserDto(user.getEmail(), user.getRoles()),
                    true
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authorization Error");
        }
    }


    public static class AuthCheckResponse {
        public boolean isAuth;

        public AuthCheckResponse(boolean isAuth) {
            this.isAuth = isAuth;
        }
    }

    public static class AuthCheckFullResponse {
        public boolean isAuth;
        public UserDto userDto;
        public boolean hasAccess;

        public AuthCheckFullResponse(boolean isAuth, UserDto userDto, boolean hasAccess) {
            this.isAuth = isAuth;
            this.userDto = userDto;
            this.hasAccess = hasAccess;
        }
    }

    @PostMapping("/account/register")
    public UserDto register(@Valid @RequestBody UserRegisterDto userRegisterDto) {
        System.out.println("Begining of registration");
        return userAccountService.register(userRegisterDto);
    }

    @PostMapping("/account/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto loginDto, HttpServletRequest request) {
        System.out.println("Beginning of login");

        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        HttpSession session = request.getSession(true);
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

        UserDto userDto = userAccountService.login(loginDto);

        String token = jwtService.generateToken(userDto.getEmail());

        return ResponseEntity.ok().body(
            new LoginResponse(token, userDto)
        );
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
