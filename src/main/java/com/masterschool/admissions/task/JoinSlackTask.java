package com.masterschool.admissions.task;

import com.masterschool.admissions.domain.TaskStatus;
import com.masterschool.admissions.dto.JoinSlackRequest;

/**
 * Task that completes Slack onboarding.
 *
 * Assumes input is already validated at the DTO level.
 */
public class JoinSlackTask implements Task<JoinSlackRequest> {

    @Override
    public TaskStatus process(JoinSlackRequest request) {
        return TaskStatus.PASSED;
    }

    @Override
    public TaskName getName() {
        return TaskName.JOIN_SLACK;
    }
}