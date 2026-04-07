package com.masterschool.admissions.task;

import com.masterschool.admissions.domain.TaskStatus;
import com.masterschool.admissions.dto.SignContractRequest;

/**
 * Task that completes contract signing.
 *.
 * Assumes request is already validated at the DTO level.
 */
public class SignContractTask implements Task<SignContractRequest> {

    @Override
    public TaskResult process(SignContractRequest request) {
        return  TaskResult.of(TaskStatus.PASSED);
    }

    @Override
    public TaskName getName() {
        return TaskName.SIGN_CONTRACT;
    }
}