package com.masterschool.admissions.flow;

import com.masterschool.admissions.task.Task;
import lombok.Getter;

import java.util.List;


/**
 * Represents a step in the flow.
 * Contains one or more tasks executed within this step.
 */

public class Step {
    private final StepName name;
    private final List<Task<?>> tasks;

    public Step(StepName name, List<Task<?>> tasks) {
        this.name = name;
        this.tasks = tasks;
    }


    public StepName getName() {
        return name;
    }

    public List<Task<?>> getTasks() {
        return tasks;
    }
}
