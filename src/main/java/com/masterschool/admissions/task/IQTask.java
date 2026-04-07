package com.masterschool.admissions.task;

import com.masterschool.admissions.domain.TaskStatus;
import com.masterschool.admissions.dto.IQRequest;

import java.util.List;

/**
 * Task responsible for evaluating the IQ test result.
 *
 * Business rule:
 * - PASS if score > PASS_THRESHOLD
 * - otherwise FAIL
 */
public class IQTask implements Task<IQRequest> {

    private static final double PASS_THRESHOLD = 75.0;
    private static final double SECOND_CHANCE_THRESHOLD = 60.0;

    @Override
    public TaskResult process(IQRequest request) {
        double score = request.score();

        if (score > PASS_THRESHOLD) {
            return TaskResult.of(TaskStatus.PASSED);
        }

        if (score >= SECOND_CHANCE_THRESHOLD) {
            return new TaskResult(
                    TaskStatus.PASSED,
                    List.of(new AddTaskToCurrentStepEffect(TaskName.SECOND_CHANCE_IQ_TEST))
            );
        }

        return TaskResult.of(TaskStatus.FAILED);
    }

    @Override
    public TaskName getName() {
        return TaskName.IQ_TEST;
    }
}