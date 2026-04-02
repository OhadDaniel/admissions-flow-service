package com.masterschool.admissions.task;

import com.masterschool.admissions.domain.TaskStatus;
import com.masterschool.admissions.dto.IQRequest;

/**
 * Task responsible for evaluating the IQ test result.
 *
 * Business rule:
 * - PASS if score > PASS_THRESHOLD
 * - otherwise FAIL
 */
public class IQTask implements Task<IQRequest> {

    private static final double PASS_THRESHOLD = 75.0;

    @Override
    public TaskStatus process(IQRequest request) {

        double score = request.score();

        return score > PASS_THRESHOLD
                ? TaskStatus.PASSED
                : TaskStatus.FAILED;
    }

    @Override
    public TaskName getName() {
        return TaskName.IQ_TEST;
    }
}