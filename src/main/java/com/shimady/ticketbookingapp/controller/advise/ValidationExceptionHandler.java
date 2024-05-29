package com.shimady.ticketbookingapp.controller.advise;

import com.shimady.ticketbookingapp.exception.ValidationError;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
@Slf4j
public class ValidationExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationError> handleConstraintViolationException(ConstraintViolationException e) {
        List<ValidationError.Violation> violations = e.getConstraintViolations()
                .stream()
                .map(this::mapConstraintViolationToViolation)
                .toList();
        log.info("Validation errors: {}", violations);
        return new ResponseEntity<>(
                new ValidationError(
                        violations,
                        HttpStatus.BAD_REQUEST.value()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<ValidationError.Violation> violations = e.getBindingResult().getFieldErrors()
                .stream()
                .map(this::mapFieldErrorToViolation)
                .toList();
        log.info("Validation errors: {}", violations);
        return new ResponseEntity<>(
                new ValidationError(
                        violations,
                        HttpStatus.BAD_REQUEST.value()
                ),
                HttpStatus.BAD_REQUEST
        );
    }

    private ValidationError.Violation mapConstraintViolationToViolation(ConstraintViolation<?> violation) {
        return new ValidationError.Violation(
                violation.getPropertyPath().toString(),
                violation.getMessage()
        );
    }

    private ValidationError.Violation mapFieldErrorToViolation(FieldError error) {
        return new ValidationError.Violation(
                error.getField(),
                error.getDefaultMessage()
        );
    }
}
