package smallITgroup.accounting.dto.exeptions;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "User not found");
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<Map<String, String>> handleInvalidPassword(InvalidPasswordException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid password");
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserExists(UserExistsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "User already exists");
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }
}
