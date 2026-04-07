package com.masterschool.admissions.runtime;

import com.masterschool.admissions.flow.StepName;
import com.masterschool.admissions.task.TaskName;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single step inside a user's runtime admissions flow.
 *
 * Unlike a static global flow definition, this class models the step
 * as it exists inside a specific user's personal flow.
 *
 * Each runtime step contains:
 * - the step identifier
 * - the ordered tasks that belong to the step
 * - a visibility flag for UI-oriented scenarios
 *
 * The visibility flag allows future support for steps that may exist
 * for only some users without necessarily appearing in the main progress UI.
 */
public class RuntimeStep {
    private final StepName name;
    private final List<TaskName> tasks;
    private final boolean visibleInProgressBar;

    /**
     * Creates a runtime step.
     *
     * @param name step identifier
     * @param tasks ordered list of tasks belonging to this step
     * @param visibleInProgressBar whether this step should be visible in progress views
     */
    public RuntimeStep(StepName name, List<TaskName> tasks, boolean visibleInProgressBar) {
        this.name = name;
        this.tasks = new ArrayList<>(tasks);
        this.visibleInProgressBar = visibleInProgressBar;
    }

    /**
     * @return the logical name of the step
     */
    public StepName getName() {
        return name;
    }

    /**
     * @return the ordered tasks belonging to this step
     */
    public List<TaskName> getTasks() {
        return tasks;
    }

    /**
     * @return whether this step should appear in progress-bar style UI representations
     */
    public boolean isVisibleInProgressBar() {
        return visibleInProgressBar;
    }

    public void addTask(TaskName taskName) {
        tasks.add(taskName);
    }
}