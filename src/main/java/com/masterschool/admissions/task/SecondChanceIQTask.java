package com.masterschool.admissions.task;

import com.masterschool.admissions.domain.TaskStatus;
import com.masterschool.admissions.dto.SecondChanceIQRequest;

/**
 * Task responsible for evaluating the second-chance IQ attempt.
 *
 * This task is executed only for users who were granted an additional
 * IQ attempt after receiving a medium score on the initial IQ task.
 *
 * Rules:
 * - score > 75  -> PASSED
 * - otherwise   -> FAILED
 */
public class SecondChanceIQTask implements Task<SecondChanceIQRequest> {

    private static final double PASS_THRESHOLD = 75.0;

    /**
     * Returns the logical identifier of this task.
     *
     * @return {@link TaskName#SECOND_CHANCE_IQ_TEST}
     */
    @Override
    public TaskName getName() {
        return TaskName.SECOND_CHANCE_IQ_TEST;
    }

    /**
     * Evaluates the second-chance IQ submission.
     *
     * @param request typed input for the second-chance IQ task
     * @return {@link TaskResult} indicating whether the second attempt passed or failed
     */
    @Override
    public TaskResult process(SecondChanceIQRequest request) {
        return request.score() > PASS_THRESHOLD
                ? TaskResult.of(TaskStatus.PASSED)
                : TaskResult.of(TaskStatus.FAILED);
    }
}