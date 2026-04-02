package com.masterschool.admissions.task;

/**
 * Enum representing all supported task types in the admissions flow.
 *.
 * Note:
 * - Acts as a type-safe alternative to using strings
 * - Should remain stable and reflect the system's supported tasks
 */
public enum TaskName {

    PERSONAL_DETAILS,
    IQ_TEST,
    SCHEDULE_INTERVIEW,
    PERFORM_INTERVIEW,
    UPLOAD_IDENTIFICATION,
    SIGN_CONTRACT,
    PAYMENT,
    JOIN_SLACK
}
