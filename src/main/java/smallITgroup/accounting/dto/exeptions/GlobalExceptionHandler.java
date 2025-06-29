package smallITgroup.accounting.dto.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice // Indicates that this class will handle global exceptions across controllers
public class GlobalExceptionHandler {

    // This method handles validation errors when invalid method arguments are passed
    @ExceptionHandler(MethodArgumentNotValidException.class) 
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "400", 
            description = "Validation error",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = "{\"error\":\"email: Email can't be empty; password: Password can't be empty\"}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, String>> handleValidationError(MethodArgumentNotValidException ex) {
        
        // Extracting the error messages from the validation exception and formatting them
        String errorMessage = ex.getBindingResult()
            .getFieldErrors() // Retrieves all field errors
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage()) // Formats error message as "field: message"
            .collect(Collectors.joining("; ")); // Joins the errors with a semicolon separator

        // Creating a response map to send back the error message
        Map<String, String> response = new HashMap<>();
        response.put("error", errorMessage); // Putting the error message into the map

        // Returning the response with a BAD_REQUEST status
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle authentication exceptions (invalid credentials)
    @ExceptionHandler({BadCredentialsException.class, InvalidPasswordException.class})
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "401", 
            description = "Authentication failed - invalid credentials",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = "{\"error\":\"Invalid credentials\"}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, String>> handleAuthenticationException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid credentials");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // Handle user not found exceptions
    @ExceptionHandler({UserNotFoundException.class, UsernameNotFoundException.class})
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "404", 
            description = "User not found",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = "{\"error\":\"User not found\"}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "User not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Handle access denied exceptions (insufficient permissions)
    @ExceptionHandler(AccessDeniedException.class)
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "403", 
            description = "Access denied - insufficient permissions",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = "{\"error\":\"Access denied\"}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Access denied");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    // Handle user already exists exceptions
    @ExceptionHandler(UserExistsException.class)
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "409", 
            description = "User already exists",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = "{\"error\":\"User already exists\"}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, String>> handleUserExistsException(UserExistsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "User already exists");
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // Handle general authentication exceptions
    @ExceptionHandler(AuthenticationException.class)
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "401", 
            description = "Authentication failed",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = ErrorResponse.class),
                examples = @ExampleObject(
                    value = "{\"error\":\"Authentication failed\"}"
                )
            )
        )
    })
    public ResponseEntity<Map<String, String>> handleGeneralAuthenticationException(AuthenticationException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Authentication failed");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // Generic error response schema for Swagger documentation
    public static class ErrorResponse {
        @Schema(description = "Error message", example = "Invalid credentials")
        private String error;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}
