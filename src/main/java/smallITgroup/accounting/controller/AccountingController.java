package smallITgroup.accounting.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User Account Management", description = "APIs for user registration, authentication, and account management")
public class AccountingController {

    // Injected service for user account operations
    final UserAccountService userAccountService;

    @Operation(
        summary = "Register a new user",
        description = "Creates a new user account with the provided email and password"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "User registered successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserDto.class),
                examples = @ExampleObject(
                    value = "{\"email\":\"user@example.com\",\"roles\":[\"USER\"]}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid input data",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"error\":\"Email can't be empty; Password have to consist 6 symbols minimum\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "409", 
            description = "User already exists",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"error\":\"User already exists\"}"
                )
            )
        )
    })
    @PostMapping("/account/register")
    public UserDto register(
        @Parameter(description = "User registration data", required = true)
        @Valid @RequestBody UserRegisterDto userRegisterDto) {
        System.out.println("Beginning of registration");
        return userAccountService.register(userRegisterDto);
    }

    @Operation(
        summary = "Login user",
        description = "Authenticates a user with email and password"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Login successful",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserDto.class),
                examples = @ExampleObject(
                    value = "{\"email\":\"user@example.com\",\"roles\":[\"USER\"]}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid input data",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"error\":\"Email can't be empty\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "401", 
            description = "Invalid credentials",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"error\":\"Invalid password\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"error\":\"User not found\"}"
                )
            )
        )
    })
    @PostMapping("/account/login")
    public UserDto login(
        @Parameter(description = "User login credentials", required = true)
        @Valid @RequestBody LoginDto loginDto) {
        System.out.println("Beginning of login");
        return userAccountService.login(loginDto);
    }

    @Operation(
        summary = "Delete user",
        description = "Removes a user account by email"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "User deleted successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"error\":\"User not found\"}"
                )
            )
        )
    })
    @DeleteMapping("/account/user/{email}")
    public UserDto removeUser(
        @Parameter(description = "Email of the user to delete", required = true)
        @PathVariable String email) {
        return userAccountService.removeUser(email);
    }

    @Operation(
        summary = "Change user password",
        description = "Changes the password for a specific user (requires authentication)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "Password changed successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = UserInfoDto.class)
            )
        ),
        @ApiResponse(
            responseCode = "400", 
            description = "Invalid input data",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"error\":\"Password can't be empty\"}"
                )
            )
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"error\":\"User not found\"}"
                )
            )
        )
    })
    @PutMapping("/account/user/{email}/password")
    public UserInfoDto changePassword(
        @Parameter(description = "Email of the user", required = true)
        @PathVariable String email,
        @Parameter(description = "New password data", required = true)
        @RequestBody ChangePasswordDto changePasswordDto) {
        return userAccountService.changePassword(email, changePasswordDto.getNewPassword());
    }

    @Operation(
        summary = "Get all users",
        description = "Retrieves a list of all users in the system"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "List of users retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "array"),
                examples = @ExampleObject(
                    value = "[{\"email\":\"user1@example.com\",\"roles\":[\"USER\"]},{\"email\":\"user2@example.com\",\"roles\":[\"ADMIN\"]}]"
                )
            )
        )
    })
    @GetMapping("/account/users")
    public List<UserInfoDto> getAllUsers() {
        return userAccountService.getAllUsers();
    }

    @Operation(
        summary = "Password recovery",
        description = "Initiates password recovery process by sending a new password to the user's email"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "202", 
            description = "Password recovery initiated successfully"
        ),
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    value = "{\"error\":\"User not found\"}"
                )
            )
        )
    })
    @GetMapping("/account/recovery/{email}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void getRecovery(
        @Parameter(description = "Email of the user for password recovery", required = true)
        @PathVariable String email) {
        userAccountService.recoveryPassword(email);
    }
    
    @Operation(
        summary = "Get all user emails",
        description = "Retrieves a list of all user email addresses"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200", 
            description = "List of emails retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(type = "array"),
                examples = @ExampleObject(
                    value = "[\"user1@example.com\",\"user2@example.com\"]"
                )
            )
        )
    })
    @GetMapping("/account/users/email")
    public List<String> getUsersEmail() {
        return userAccountService.getUsersEmail();
    }
}
