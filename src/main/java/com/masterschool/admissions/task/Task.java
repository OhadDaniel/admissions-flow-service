package com.masterschool.admissions.task;

import com.masterschool.admissions.domain.TaskStatus;

/**
 * Represents a generic task in the admissions flow.
 *
 * @param <T> the type of request this task handles
 */
public interface Task<T> {

    TaskName getName();

    TaskStatus process(T request);
}
