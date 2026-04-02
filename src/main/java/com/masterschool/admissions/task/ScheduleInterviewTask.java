package com.masterschool.admissions.task;

import com.masterschool.admissions.domain.TaskStatus;
import com.masterschool.admissions.dto.ScheduleInterviewRequest;

/**
 * Task that schedules an interview.
 *
 * Assumes request is already validated at the DTO level.
 */
public class ScheduleInterviewTask implements Task<ScheduleInterviewRequest> {

    @Override
    public TaskStatus process(ScheduleInterviewRequest request) {
        return TaskStatus.PASSED;
    }

    @Override
    public TaskName getName() {
        return TaskName.SCHEDULE_INTERVIEW;
    }
}