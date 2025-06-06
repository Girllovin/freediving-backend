package smallITgroup.accounting.dto.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    // @ExceptionHandler(UserNotFoundException.class)
}
