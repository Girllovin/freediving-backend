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

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice // Indicates that this class will handle global exceptions across controllers
public class GlobalExceptionHandler {

    // This method handles validation errors when invalid method arguments are passed
    @ExceptionHandler(MethodArgumentNotValidException.class) 
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
    public ResponseEntity<Map<String, String>> handleAuthenticationException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid credentials");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // Handle user not found exceptions
    @ExceptionHandler({UserNotFoundException.class, UsernameNotFoundException.class})
    public ResponseEntity<Map<String, String>> handleUserNotFoundException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "User not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Handle access denied exceptions (insufficient permissions)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Access denied");
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    // Handle user already exists exceptions
    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserExistsException(UserExistsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "User already exists");
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    // Handle general authentication exceptions
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, String>> handleGeneralAuthenticationException(AuthenticationException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Authentication failed");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    // Generic error response schema for Swagger documentation
    public static class ErrorResponse {
        private String error;

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }
    }
}
