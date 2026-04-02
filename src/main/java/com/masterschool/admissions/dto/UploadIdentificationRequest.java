package com.masterschool.admissions.dto;

import com.masterschool.admissions.validations.ValidationUtils;

import java.time.Instant;

/**
 * DTO representing the identification document upload.
 *
 * Contains:
 * - passportNumber: user's passport identifier
 * - timestamp: upload time
 *
 * Validation is performed upon object creation.
 */
public record UploadIdentificationRequest(
        String passportNumber,
        Instant timestamp
) {
    public UploadIdentificationRequest {
        ValidationUtils.requireNonBlank(passportNumber, "passportNumber");
        ValidationUtils.requireNonNull(timestamp, "timestamp");
    }
}