package com.masterschool.admissions.dto;

import com.masterschool.admissions.flow.Step;

/**
 * Represents the user's current position in the admissions flow.
 */
public record CurrentStateResponse(
        Step step,
        String currentTask
) {}