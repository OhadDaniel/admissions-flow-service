package com.masterschool.admissions.exception;

/**
 * Thrown when a task does not belong to the user's current step in the flow.
 *
 * Indicates an invalid operation where the client attempts to execute a task
 * that is not part of the current step.
 */
public class TaskNotAllowedException extends RuntimeException {
    public TaskNotAllowedException(String message) {
        super(message);
    }
}
