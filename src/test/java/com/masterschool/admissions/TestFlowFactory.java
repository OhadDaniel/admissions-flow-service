package com.masterschool.admissions;

import com.masterschool.admissions.flow.FlowDefinition;
import com.masterschool.admissions.flow.Step;
import com.masterschool.admissions.flow.StepName;
import com.masterschool.admissions.task.*;

import java.util.List;

/**
 * Test factory for building a full admissions flow.
 *
 * Provides a ready-to-use FlowDefinition for tests.
 */
public class TestFlowFactory {

    public static FlowDefinition createFlow() {
        return new FlowDefinition(List.of(

                new Step(StepName.PERSONAL_DETAILS, List.of(new PersonalDetailsTask())),

                new Step(StepName.IQ_TEST, List.of(new IQTask())),

                new Step(StepName.INTERVIEW, List.of(new ScheduleInterviewTask(), new PerformInterviewTask())),

                new Step(StepName.SIGN_CONTRACT, List.of(new UploadIdentificationTask(), new SignContractTask())),

                new Step(StepName.PAYMENT, List.of(new PaymentTask())),

                new Step(StepName.JOIN_SLACK, List.of(new JoinSlackTask()))
        ));
    }
}