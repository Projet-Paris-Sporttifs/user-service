package psp.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import psp.user.payload.response.MessageResponse;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UniqueConstraintViolationException.class)
    public Map<String, String> handleUniqueConstraintViolation(UniqueConstraintViolationException ex) {
        Map<String, String> error = new HashMap<>();
        error.put(ex.getFieldName(), ex.getMessage());
        return error;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public MessageResponse handleUserNotFoundException(UserNotFoundException ex) {
        return new MessageResponse(ex.getFieldName() + " '" + ex.getFieldValue() + "' does not exists");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PasswordNotMatchingException.class)
    public Map<String, String> handlePasswordNotMatchingException(PasswordNotMatchingException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("password", "Passwords do not match.");
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PaginationParamsException.class)
    public MessageResponse handlePaginationParamsException(PaginationParamsException ex) {
        return new MessageResponse("Pagination parameters are invalid.");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(UpdateValidationException.class)
    public Map<String, String> handleUpdateValidationException(UpdateValidationException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(ex.getFieldName(), ex.getMessage());
        return response;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public Map<String, String> handleBadRequestException(BadRequestException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", ex.getMessage());
        return response;
    }
}
