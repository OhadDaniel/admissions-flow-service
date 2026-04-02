package com.masterschool.admissions.exception;

import com.masterschool.admissions.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Global handler that maps application exceptions to HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles validation errors (DTO / input validation).
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(IllegalArgumentException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    /**
     * Handles cases where a user does not exist.
     */
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFound(UserNotFoundException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    /**
     * Handles invalid task for current step.
     */
    @ExceptionHandler(TaskNotAllowedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTaskNotAllowed(TaskNotAllowedException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    /**
     * Handles incorrect task execution order.
     */
    @ExceptionHandler(InvalidTaskOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleInvalidOrder(InvalidTaskOrderException ex) {
        return new ErrorResponse(ex.getMessage());
    }

    /**
     * Fallback for unexpected errors.
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneral(Exception ex) {
        return new ErrorResponse("Internal server error");
    }
}