package com.masterschool.admissions.domain;

/**
 * Represents the status of a specific task execution for a user.
 * .
 * PENDING   - The task has not been completed yet.
 * PASSED    - The task was completed successfully and met its criteria.
 * FAILED    - The task was completed but did not meet its criteria.
 */
public enum TaskStatus {
    PENDING,
    PASSED,
    FAILED
}
