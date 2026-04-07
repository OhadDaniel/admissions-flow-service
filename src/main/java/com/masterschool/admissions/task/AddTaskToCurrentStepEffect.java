package com.masterschool.admissions.task;

/**
 * Effect requesting the facade to add a task to the user's current runtime step.
 */
public record AddTaskToCurrentStepEffect(TaskName taskName) implements TaskEffect {
    public AddTaskToCurrentStepEffect {
        if (taskName == null) {
            throw new IllegalArgumentException("taskName cannot be null");
        }
    }
}