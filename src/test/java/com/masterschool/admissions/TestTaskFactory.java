package com.masterschool.admissions;

import com.masterschool.admissions.task.*;

import java.util.Map;

/**
 * Test factory for creating a TaskFactory with all tasks registered.
 *
 * Provides a ready-to-use TaskFactory for testing purposes.
 */
public class TestTaskFactory {

    public static TaskFactory create() {
        return new TaskFactory(Map.of(
                TaskName.PERSONAL_DETAILS, new PersonalDetailsTask(),
                TaskName.IQ_TEST, new IQTask(),
                TaskName.SCHEDULE_INTERVIEW, new ScheduleInterviewTask(),
                TaskName.PERFORM_INTERVIEW, new PerformInterviewTask(),
                TaskName.UPLOAD_IDENTIFICATION, new UploadIdentificationTask(),
                TaskName.SIGN_CONTRACT, new SignContractTask(),
                TaskName.PAYMENT, new PaymentTask(),
                TaskName.JOIN_SLACK, new JoinSlackTask()
        ));
    }
}