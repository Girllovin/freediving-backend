package smallITgroup.accounting.dto.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// @ResponseStatus annotation indicates the HTTP status to be returned when this exception is thrown.
// In this case, it's a 404 Not Found status, indicating that the resource (user) could not be found.
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserNotFoundException extends RuntimeException {

    // serialVersionUID is used to ensure that during deserialization, the class version is compatible.
    private static final long serialVersionUID = 2755166764452062685L;

}
