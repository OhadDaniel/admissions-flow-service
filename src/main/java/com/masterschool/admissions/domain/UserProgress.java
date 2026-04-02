package com.masterschool.admissions.domain;

import com.masterschool.admissions.flow.StepName;
import com.masterschool.admissions.task.TaskName;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents the progress of a user in the admissions flow.
 *.
 * Tracks the user's current step and task, overall status,
 * and all executed tasks along with their results.
 *.
 * Note:
 * - Does not contain flow definition logic.
 * - Maintains a history of task executions per user.
 */

@Getter
public class UserProgress {

    private final String userId;

    private StepName currentStep;
    private TaskName currentTask;
    private UserStatus status;

    private final  Map<TaskName, TaskInstance> tasks;

    public UserProgress(String userId) {
        this.userId = userId;
        this.status = UserStatus.IN_PROGRESS;
        this.tasks = new HashMap<>();
    }

    public void advance(StepName step, TaskName task) {
        if (step == null) {
            throw new IllegalArgumentException("Step cannot be null");
        }
        this.currentStep = step;
        this.currentTask = task;
    }

    public void markAccepted() {
        this.status = UserStatus.ACCEPTED;
    }

    public void markRejected() {
        this.status = UserStatus.REJECTED;
    }


    public void addTaskInstance(TaskInstance taskInstance) {
        tasks.put(taskInstance.getTaskName(), taskInstance);
    }
}
