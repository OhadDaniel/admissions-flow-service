package com.masterschool.admissions.flow;

import com.masterschool.admissions.runtime.RuntimeStep;
import com.masterschool.admissions.runtime.UserFlow;
import com.masterschool.admissions.task.TaskName;

import java.util.List;

/**
 * Creates the runtime admissions flow assigned to each user.
 */
public class FlowConfig {

    public static UserFlow createUserFlow() {
        return new UserFlow(List.of(
                new RuntimeStep(StepName.PERSONAL_DETAILS, List.of(TaskName.PERSONAL_DETAILS), true),
                new RuntimeStep(StepName.IQ_TEST, List.of(TaskName.IQ_TEST), true),
                new RuntimeStep(StepName.INTERVIEW, List.of(TaskName.SCHEDULE_INTERVIEW, TaskName.PERFORM_INTERVIEW), true),
                new RuntimeStep(StepName.SIGN_CONTRACT, List.of(TaskName.UPLOAD_IDENTIFICATION, TaskName.SIGN_CONTRACT), true),
                new RuntimeStep(StepName.PAYMENT, List.of(TaskName.PAYMENT), true),
                new RuntimeStep(StepName.JOIN_SLACK, List.of(TaskName.JOIN_SLACK), true)
        ));
    }
}