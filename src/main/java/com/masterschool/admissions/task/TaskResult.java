package com.masterschool.admissions.task;
import com.masterschool.admissions.domain.TaskStatus;

import java.util.List;

/**
 * Represents the outcome of a task execution.
 *
 * A task result contains:
 * - the task status itself
 * - side effects that should be applied by the orchestration layer
 */
public record TaskResult(
        TaskStatus status,
        List<TaskEffect> effects
) {
    public TaskResult {
        if (status == null) {
            throw new IllegalArgumentException("status cannot be null");
        }
        if (effects == null) {
            throw new IllegalArgumentException("effects cannot be null");
        }
    }

    public static TaskResult of(TaskStatus status) {
        return new TaskResult(status, List.of());
    }
}
