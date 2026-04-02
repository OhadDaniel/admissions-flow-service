package com.masterschool.admissions.dto;

import com.masterschool.admissions.domain.UserStatus;
import com.masterschool.admissions.runtime.RuntimeStep;

import java.util.List;

/**
 * Represents the full flow along with the user's current state.
 */
public record FlowResponse(
        List<RuntimeStep> steps,
        int currentStepIndex,
        String currentTask,
        UserStatus status
) {}