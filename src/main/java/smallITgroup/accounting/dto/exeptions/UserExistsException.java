package smallITgroup.accounting.dto.exeptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// @ResponseStatus is an annotation that allows you to specify the HTTP status code to return when this exception is thrown.
// In this case, the status code is 409 Conflict, indicating that the requested operation could not be completed due to a conflict.
@ResponseStatus(HttpStatus.CONFLICT)
public class UserExistsException extends RuntimeException {

    // serialVersionUID is a unique identifier for the class version. It is used during deserialization to ensure that a loaded class 
    // corresponds to the serialized object.
    private static final long serialVersionUID = 8777376761193864981L;

}
