package com.masterschool.admissions.exception;

/**
 * Thrown when a user cannot be found in the system.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
