package com.masterschool.admissions.dto;


import com.masterschool.admissions.validations.ValidationUtils;

import java.time.Instant;

/**
 * DTO representing the input for the second-chance IQ task.
 */
public record SecondChanceIQRequest(
        String testId,
        Double score,
        Instant timestamp
) {
    public SecondChanceIQRequest {
        ValidationUtils.requireNonBlank(testId, "testId");
        ValidationUtils.requireNonNull(score, "score");
        ValidationUtils.requireNonNull(timestamp, "timestamp");
    }
}