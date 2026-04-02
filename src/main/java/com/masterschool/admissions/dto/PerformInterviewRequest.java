package com.masterschool.admissions.dto;

import com.masterschool.admissions.validations.ValidationUtils;

import java.time.Instant;

/**
 * DTO representing the input for performing an interview.
 *
 * Contains:
 * - interviewDate: scheduled interview time
 * - interviewerId: identifier of the interviewer
 * - decision: interview outcome
 *
 * Validation is performed upon object creation.
 */
public record PerformInterviewRequest(
        Instant interviewDate,
        String interviewerId,
        String decision
) {
    public PerformInterviewRequest {
        ValidationUtils.requireNonNull(interviewDate, "interviewDate");
        ValidationUtils.requireNonBlank(interviewerId, "interviewerId");
        ValidationUtils.requireNonBlank(decision, "decision");
    }
}