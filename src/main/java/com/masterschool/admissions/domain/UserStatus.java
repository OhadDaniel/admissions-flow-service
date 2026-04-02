package com.masterschool.admissions.domain;


/**
 * Represents the overall status of a user in the admissions process.
 * .
 * IN_PROGRESS - The user is currently going through the flow.
 * ACCEPTED    - The user has successfully completed all required steps.
 * REJECTED    - The user failed one of the required steps and exited the flow.
 */
public enum UserStatus {
    IN_PROGRESS,
    ACCEPTED,
    REJECTED
}
