package com.masterschool.admissions.dto;

import com.masterschool.admissions.runtime.RuntimeStep;

/**
 * Represents the user's current position in the admissions flow.
 */
public record CurrentStateResponse(
        RuntimeStep step,
        String currentTask
) {}