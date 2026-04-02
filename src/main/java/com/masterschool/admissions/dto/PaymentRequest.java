package com.masterschool.admissions.dto;

import com.masterschool.admissions.validations.ValidationUtils;

import java.time.Instant;

/**
 * DTO representing a payment action.
 *
 * Validation is performed upon creation to ensure only valid data
 * reaches the business logic layer.
 */
public record PaymentRequest(
        String paymentId,
        Instant timestamp
) {
    public PaymentRequest {
        ValidationUtils.requireNonBlank(paymentId, "paymentId");
        ValidationUtils.requireNonNull(timestamp, "timestamp");
    }
}