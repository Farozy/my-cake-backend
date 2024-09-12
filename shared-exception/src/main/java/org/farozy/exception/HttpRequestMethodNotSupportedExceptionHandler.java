package org.farozy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class HttpRequestMethodNotSupportedExceptionHandler {
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public ResponseEntity<Object> handleMethodNotAllowedException(HttpRequestMethodNotSupportedException ex) {
        Map<String, Object> response = ErrorResponseBuilder.validationErrorResponse(
                HttpStatus.METHOD_NOT_ALLOWED, "Method Not Allowed", ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

}
