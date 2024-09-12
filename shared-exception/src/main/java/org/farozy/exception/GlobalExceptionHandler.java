package org.farozy.exception;

import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.security.SignatureException;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final MethodArgumentNotValidExceptionHandler methodArgumentNotValidExceptionHandler;
    private final HttpRequestMethodNotSupportedExceptionHandler httpRequestMethodNotSupportedExceptionHandler;
    private final ResourceNotFoundExceptionHandler resourceNotFoundExceptionHandler;
    private final ResourceAlreadyExistsExceptionHandler resourceAlreadyExistsExceptionHandler;
    private final MultipartExceptionHandler multipartExceptionHandler;
    private final InternalServerExceptionHandler internalServerExceptionHandler;
    private final MaxUploadSizeExceededExceptionHanlder maxUploadSizeExceededExceptionHanlder;
    private final ExpiredJwtExceptionHandler expiredJwtExceptionHandler;
    private final InvalidTokenExceptionHandler invalidTokenExceptionHandler;
    private final AccessDeniedExceptionHandler accessDeniedExceptionHandler;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException ex) {
        return this.resourceNotFoundExceptionHandler.handleResourceNotFoundException(ex);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        return methodArgumentNotValidExceptionHandler.handleValidationExceptions(ex);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Object> handleMethodNotAllowedException(HttpRequestMethodNotSupportedException ex) {
        return httpRequestMethodNotSupportedExceptionHandler.handleMethodNotAllowedException(ex);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Object> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex) {
        return resourceAlreadyExistsExceptionHandler.handleResourceAlreadyExistsExceptions(ex);
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<Object> handleMultipartException(MultipartException ex) {
        return multipartExceptionHandler.handleMultipartException(ex);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        return internalServerExceptionHandler.handleInternalServerException(ex);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String, Object>> handleMaxUploadSizeExceeded() {
        return maxUploadSizeExceededExceptionHanlder.handleUploadSizeExceededException();
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<Object> handleExpiredJwtException(ExpiredJwtException ex) {
        return expiredJwtExceptionHandler.handleExpiredException(ex);
    }

    @ExceptionHandler({
            SignatureException.class, io.jsonwebtoken.MalformedJwtException.class,
            UnsupportedJwtException.class, IllegalArgumentException.class
    })
    public ResponseEntity<Object> handleInvalidJwtTokenException(Exception e) {
        Map<String, Object> response = ErrorResponseBuilder.validationErrorResponse(
                HttpStatus.BAD_REQUEST, "An error occurred while verifying the token or email", e.getMessage()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<Object> handleInvalidTokenException(InvalidTokenException ex) {
        return this.invalidTokenExceptionHandler.handleInvalidTokenException(ex);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex) {
        return this.accessDeniedExceptionHandler.handleExpiredException(ex);
    }

}
