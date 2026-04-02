package com.masterschool.admissions.task;

import java.util.Map;

/**
 * Factory responsible for providing task instances by their TaskName.
 *.
 * Encapsulates the mapping between task identifiers and their implementations.
 */
public class TaskFactory {

    private final Map<TaskName, Task<?>> tasks;

    public TaskFactory(Map<TaskName, Task<?>> tasks) {
        this.tasks = tasks;
    }

    /**
     * Retrieves the task associated with the given name.
     *
     * @param name the task identifier
     * @return the corresponding task instance
     * @throws IllegalArgumentException if no task is found
     */
    @SuppressWarnings("unchecked")
    public <T> Task<T> get(TaskName name) {

        // Lookup task by name
        Task<?> task = tasks.get(name);

        // Fail fast if task is not registered
        if (task == null) {
            throw new IllegalArgumentException("Task not found: " + name);
        }

        // Safe cast based on contract between TaskName and DTO type
        return (Task<T>) task;
    }
}