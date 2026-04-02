package com.masterschool.admissions.dto;

import com.masterschool.admissions.domain.TaskStatus;

/**
 * Represents the result of a task execution.
 */
public record TaskResponse(
        TaskStatus status
) {}