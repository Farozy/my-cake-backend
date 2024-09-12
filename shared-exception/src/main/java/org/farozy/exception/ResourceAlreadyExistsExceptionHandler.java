package org.farozy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class ResourceAlreadyExistsExceptionHandler {

    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ResponseEntity<Object> handleResourceAlreadyExistsExceptions(ResourceAlreadyExistsException ex) {
        Map<String, Object> response = ErrorResponseBuilder.validationErrorResponse(HttpStatus.CONFLICT, "Conflict occurred", ex.getMessage());

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

}
