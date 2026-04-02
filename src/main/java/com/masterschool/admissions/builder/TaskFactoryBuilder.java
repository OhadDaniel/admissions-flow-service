package com.masterschool.admissions.builder;

import com.masterschool.admissions.task.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Responsible for constructing and configuring the TaskFactory.
 *.
 * This class centralizes the registration of all tasks in the system,
 * mapping each TaskName to its corresponding Task implementation.
 *.
 * Design notes:
 * - Acts as a composition root for task creation.
 * - Prevents duplication of task registration logic across the codebase.
 * - Makes it easy to add/remove tasks in a single place.
 *.
 * Usage:
 * TaskFactory factory = TaskFactoryBuilder.build();
 */
public class TaskFactoryBuilder {

    public static TaskFactory build() {

        Map<TaskName, Task<?>> tasks = new HashMap<>();

        tasks.put(TaskName.PERSONAL_DETAILS, new PersonalDetailsTask());
        tasks.put(TaskName.IQ_TEST, new IQTask());
        tasks.put(TaskName.SCHEDULE_INTERVIEW, new ScheduleInterviewTask());
        tasks.put(TaskName.PERFORM_INTERVIEW, new PerformInterviewTask());
        tasks.put(TaskName.UPLOAD_IDENTIFICATION, new UploadIdentificationTask());
        tasks.put(TaskName.SIGN_CONTRACT, new SignContractTask());
        tasks.put(TaskName.PAYMENT, new PaymentTask());
        tasks.put(TaskName.JOIN_SLACK, new JoinSlackTask());

        return new TaskFactory(tasks);
    }
}
