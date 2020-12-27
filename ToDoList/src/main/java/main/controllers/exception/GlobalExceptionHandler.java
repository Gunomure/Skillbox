package main.controllers.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(main.controllers.exception.EntityNotFoundException.class)
    protected ResponseEntity<main.controllers.exception.CustomErrorResponse> handleEntityNotFoundException(Exception ex) {
        main.controllers.exception.CustomErrorResponse errors = new main.controllers.exception.CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(ex.getMessage());
        errors.setStatus(HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(errors, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    protected ResponseEntity<main.controllers.exception.CustomErrorResponse> handleBadRequestxception(Exception ex) {
        main.controllers.exception.CustomErrorResponse errors = new main.controllers.exception.CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(ex.getMessage());
        errors.setStatus(HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ArgumentNotValidException.class)
    public ResponseEntity<main.controllers.exception.CustomErrorResponse> handleValidationExceptions(Exception ex) {
        main.controllers.exception.CustomErrorResponse errors = new main.controllers.exception.CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(ex.getMessage());
        errors.setStatus(HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
