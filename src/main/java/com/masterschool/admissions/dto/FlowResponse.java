package com.masterschool.admissions.dto;

import com.masterschool.admissions.domain.UserStatus;
import com.masterschool.admissions.flow.Step;

import java.util.List;

/**
 * Represents the full flow along with the user's current state.
 */
public record FlowResponse(
        List<Step> steps,
        int currentStepIndex,
        String currentTask,
        UserStatus status
) {}