package com.masterschool.admissions.task;

import com.masterschool.admissions.domain.TaskStatus;
import com.masterschool.admissions.dto.PersonalDetailsRequest;

/**
 * Task that completes personal details submission.
 *
 * Assumes request is already validated at the DTO level.
 */
public class PersonalDetailsTask implements Task<PersonalDetailsRequest> {

    @Override
    public TaskStatus process(PersonalDetailsRequest request) {
        return TaskStatus.PASSED;
    }

    @Override
    public TaskName getName() {
        return TaskName.PERSONAL_DETAILS;
    }
}