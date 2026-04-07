package com.masterschool.admissions.task;

import com.masterschool.admissions.domain.TaskStatus;
import com.masterschool.admissions.dto.UploadIdentificationRequest;

/**
 * Task that completes identification document submission.
 *
 * Assumes request is already validated at the DTO level.
 */
public class UploadIdentificationTask implements Task<UploadIdentificationRequest> {

    @Override
    public TaskResult process(UploadIdentificationRequest request) {
        return TaskResult.of(TaskStatus.PASSED);
    }

    @Override
    public TaskName getName() {
        return TaskName.UPLOAD_IDENTIFICATION;
    }
}

