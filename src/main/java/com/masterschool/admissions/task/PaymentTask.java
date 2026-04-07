package com.masterschool.admissions.task;

import com.masterschool.admissions.domain.TaskStatus;
import com.masterschool.admissions.dto.PaymentRequest;

/**
 * Task that completes payment submission.
 *
 * Assumes request is already validated at the DTO level.
 */
public class PaymentTask implements Task<PaymentRequest> {

    @Override
    public TaskResult process(PaymentRequest request) {
        return TaskResult.of(TaskStatus.PASSED);
    }

    @Override
    public TaskName getName() {
        return TaskName.PAYMENT;
    }
}