package com.compartetutiempo.timebank.exceptions;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> globalExceptionHandler(Exception exception, WebRequest request) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, exception, request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException exception, WebRequest request) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, exception, request);
    }

    @ExceptionHandler(ResourceNotOwnedException.class)
    public ResponseEntity<ErrorMessage> resourceNotOwnedExceptionHandler(ResourceNotOwnedException exception, WebRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, exception, request);
    }

    @ExceptionHandler(CreditLimitReachedException.class)
    public ResponseEntity<ErrorMessage> creditLimitReachedExceptionHandler(CreditLimitReachedException exception, WebRequest request) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, exception, request);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public final ResponseEntity<ErrorMessage> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex, WebRequest request) {
        Map<String, Object> fieldError = new HashMap<>();
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        fieldErrors.stream().forEach(error -> fieldError.put(error.getField(), error.getDefaultMessage()));

        return buildResponseEntity(HttpStatus.BAD_REQUEST, fieldError.toString(), request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> accessDeniedExceptionHandler(AccessDeniedException exception, WebRequest request) {
        return buildResponseEntity(HttpStatus.FORBIDDEN, exception, request);
    }

    private ResponseEntity<ErrorMessage> buildResponseEntity(HttpStatus status, Exception exception, WebRequest request) {
        return buildResponseEntity(status, exception.getMessage(), request);
    }

    private ResponseEntity<ErrorMessage> buildResponseEntity(HttpStatus status, String errorMessage, WebRequest request) {
        ErrorMessage message = new ErrorMessage(
            status.value(),
            LocalDateTime.now(),
            errorMessage,
            request.getDescription(false)
        );
        return new ResponseEntity<>(message, status);
    }

}
