package com.masterschool.admissions.dto;

import com.masterschool.admissions.validations.ValidationUtils;

import java.time.Instant;

/**
 * DTO representing joining Slack.
 *
 * Validation is performed during object construction to ensure only valid
 * data reaches the business logic layer.
 */
public record JoinSlackRequest(
        String email,
        Instant timestamp
) {
    public JoinSlackRequest {
        ValidationUtils.requireNonBlank(email, "email");
        ValidationUtils.requireNonNull(timestamp, "timestamp");
    }
}