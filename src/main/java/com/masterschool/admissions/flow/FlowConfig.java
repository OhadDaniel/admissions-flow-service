package com.masterschool.admissions.flow;

import com.masterschool.admissions.task.*;

import java.util.List;

/**
 * Builds the admissions flow configuration.
 */
public class FlowConfig {

    public static FlowDefinition build(
            Task<?> personalDetailsTask,
            Task<?> iqTask,
            Task<?> scheduleInterviewTask,
            Task<?> performInterviewTask,
            Task<?> uploadIdentificationTask,
            Task<?> signContractTask,
            Task<?> paymentTask,
            Task<?> joinSlackTask
    ){

        return new FlowDefinition(List.of(

                new Step(StepName.PERSONAL_DETAILS, List.of(personalDetailsTask)),

                new Step(StepName.IQ_TEST, List.of(iqTask)),

                new Step(StepName.INTERVIEW, List.of(scheduleInterviewTask, performInterviewTask)),

                new Step(StepName.SIGN_CONTRACT, List.of(uploadIdentificationTask, signContractTask)),

                new Step(StepName.PAYMENT, List.of(paymentTask)),

                new Step(StepName.JOIN_SLACK, List.of(joinSlackTask))
        ));
    }
}