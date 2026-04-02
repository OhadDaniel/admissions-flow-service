package com.masterschool.admissions.exception;

/**
 * Thrown when a task is executed out of the required order within a step.
 *
 * Indicates that one or more preceding tasks have not been completed successfully,
 * and therefore the current task cannot be processed.
 */
public class InvalidTaskOrderException extends RuntimeException {
    public InvalidTaskOrderException(String message) {
        super(message);
    }
}
