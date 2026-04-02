package com.masterschool.admissions.domain;

import com.masterschool.admissions.task.TaskName;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;
import java.time.Instant;

/**
 * Represents the result of executing a specific task for a user.
 *.
 * Contains the task identifier, its execution status,
 * the request data associated with the task, and the timestamp of completion.
 */
@Getter
public class TaskInstance {

    private final TaskName taskName;
    private TaskStatus status;
    private Object payload; // payload holds the request DTO used to execute the task
    private final Instant timestamp;

    public TaskInstance(TaskName taskName,
                        TaskStatus status,
                        Object payload,
                        Instant timestamp) {
        this.taskName = taskName;
        this.status = status;
        this.payload = payload;
        this.timestamp = timestamp;
    }
}
