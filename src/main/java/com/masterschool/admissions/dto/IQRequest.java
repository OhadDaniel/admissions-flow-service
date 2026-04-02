package com.masterschool.admissions.dto;

import com.masterschool.admissions.validations.ValidationUtils;

import java.time.Instant;

/**
 * DTO representing the input for the IQ test task.
 *
 * Contains:
 * - testId: identifier of the test
 * - score: user's score
 * - timestamp: submission time
 *
 * Validation is performed upon object creation to ensure only valid data
 * reaches the business logic layer.
 */
public record IQRequest(
        String testId,
        Double score,
        Instant timestamp
) {
    public IQRequest {
        ValidationUtils.requireNonBlank(testId, "testId");
        ValidationUtils.requireNonNull(score, "score");
        ValidationUtils.requireNonNull(timestamp, "timestamp");
    }
}