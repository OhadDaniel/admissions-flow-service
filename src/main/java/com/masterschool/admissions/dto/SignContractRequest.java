package com.masterschool.admissions.dto;

import com.masterschool.admissions.validations.ValidationUtils;

import java.time.Instant;

/**
 * DTO representing the contract signing action.
 *
 * Contains:
 * - timestamp: time of contract signing
 *
 * Validation is performed upon object creation.
 */
public record SignContractRequest(
        Instant timestamp
) {
    public SignContractRequest {
        ValidationUtils.requireNonNull(timestamp, "timestamp");
    }
}