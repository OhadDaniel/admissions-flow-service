package com.masterschool.admissions.dto;

import com.masterschool.admissions.validations.ValidationUtils;

import java.time.Instant;

/**
 * DTO representing scheduling an interview.
 *
 * Contains:
 * - interviewDate: the scheduled interview time
 *
 * Validation is performed upon object creation.
 */
public record ScheduleInterviewRequest(
        Instant interviewDate
) {
    public ScheduleInterviewRequest {
        ValidationUtils.requireNonNull(interviewDate, "interviewDate");
    }
}