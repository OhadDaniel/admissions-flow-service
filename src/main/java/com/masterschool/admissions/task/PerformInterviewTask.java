package com.masterschool.admissions.task;

import com.masterschool.admissions.domain.TaskStatus;
import com.masterschool.admissions.dto.PerformInterviewRequest;

/**
 * Task that evaluates interview outcome.
 *
 * Business rule:
 * - PASSED if decision equals "passed_interview"
 * - otherwise FAILED
 */
public class PerformInterviewTask implements Task<PerformInterviewRequest> {

    @Override
    public TaskStatus process(PerformInterviewRequest request) {

        return "passed_interview".equals(request.decision())
                ? TaskStatus.PASSED
                : TaskStatus.FAILED;
    }

    @Override
    public TaskName getName() {
        return TaskName.PERFORM_INTERVIEW;
    }
}