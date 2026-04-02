package com.masterschool.admissions.dto;

import com.masterschool.admissions.validations.ValidationUtils;

import java.time.Instant;

/**
 * DTO representing the Personal Details Form submission.
 *
 * Contains user-provided personal information required for the admissions flow.
 *
 * Validation is performed upon object creation to ensure only valid data
 * reaches the business logic layer.
 */
public record PersonalDetailsRequest(
        String firstName,
        String lastName,
        String email,
        Instant timestamp
) {
    public PersonalDetailsRequest {
        ValidationUtils.requireNonBlank(firstName, "firstName");
        ValidationUtils.requireNonBlank(lastName, "lastName");
        ValidationUtils.requireNonBlank(email, "email");
        ValidationUtils.requireNonNull(timestamp, "timestamp");
    }
}